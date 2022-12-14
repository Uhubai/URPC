package com.uhubai.client.register;

import com.uhubai.common.utils.ClassUtils;
import com.uhubai.common.utils.RpcContext;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceDiscovery {

    private final String serviceRootPacket = "com.uhubai.rpc.service.impl";
    private String registryAddress = "127.0.0.1:2181";
    private String ZK_REGISTRY_PATH = "/rpc";
    private int SESSION_TIMEOUT = 10000;
    private int CONNECTION_TIMEOUT = 10000;

    private volatile List<String> serviceList = new ArrayList<>();

    private ConcurrentHashMap<String, List<Invoker>> serverMap = new ConcurrentHashMap<>();

    private ZkClient zkClient;

    private static ServiceDiscovery instance = null;

    public static ServiceDiscovery getInstance() {
        if (instance == null) {
            instance = new ServiceDiscovery();
        }
        return instance;
    }

    private ServiceDiscovery() {
        connectServer();
    }


    public void discovery(String serviceName) {
        //获取本地所有服务
        //importService(serviceName);
        // 发布客户端引用到注册中心
        registerReference(serviceName);
        // 获取引用
        // 缓存引用
        getReference(serviceName);
        // 订阅服务变化
        subscribeServiceChange(serviceName);
    }

    public void connectServer(RegistryConfig registryConfig) {
        this.registryAddress = registryConfig.getRegistryAddress();
        this.SESSION_TIMEOUT = registryConfig.getSessionTimeout();
        this.CONNECTION_TIMEOUT = registryConfig.getConnectionTimeout();
        this.ZK_REGISTRY_PATH = registryConfig.getZkPath();
        connectServer();
    }

    public void connectServer() {
        if (zkClient == null) {
            zkClient = new ZkClient(registryAddress, SESSION_TIMEOUT, CONNECTION_TIMEOUT);
            log.info("zookeeper 已连接");
        }
    }

    private void importService(String name) {
        boolean flag = false;
        List<Class<?>> services = ClassUtils.getClasses(name.substring(0,name.lastIndexOf(".")));
        for (Class<?> clazz : services) {
            if(clazz.getName().equals(name)){
                serviceList.add(name);
                flag = true;
                break;
            }
        }
        if(!flag){
            log.error("No such interface {} ,please check your config..", name);
            throw new RuntimeException("No such interface");
        }
    }



    private void registerReference(String service) {
        String ip = RpcContext.getLocalIp();

        String basePath = ZK_REGISTRY_PATH + "/" + service + "/consumers";
        String path = basePath + "/" + ip;

        createPersistent(basePath);
        //临时节点
        if (!zkClient.exists(path)) {
            zkClient.createEphemeral(path);
        }
        log.info("客户端引用发布成功:[{}]", path);
    }



    private void getReference(String service) {
        //删除旧的缓存
        if(serverMap.containsKey(service)) {
            serverMap.remove(service);
        }
        log.info("正在获取服务引用[{}]", service);
        String path = ZK_REGISTRY_PATH + "/" + service + "/providers";
        List<String> serverList = null;
        try {
            serverList = zkClient.getChildren(path);
        } catch (Exception e) {
            log.error("服务器无此服务，请检查相关配置");
            e.printStackTrace();
        }
        log.info("发现服务器列表" + serverList);
        if(serverList == null){
            log.error("服务[" + service + "] 找不到服务列表");
        }else {
            serverMap.put(service, toInvokers(service, serverList));
            log.info("引用服务获取完成[" + path + "]:" + service);
        }

    }


    private void subscribeServiceChange(String service) {
        String path = ZK_REGISTRY_PATH + "/" + service + "/providers";
        log.info("订阅服务变化[{}]", service);
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                serverMap.remove(service);
                serverMap.put(service, toInvokers(service, currentChilds));
                log.info("服务[{}]发生变化，当前服务节点为{}", service, currentChilds);
            }
        });
    }

    private List<Invoker> toInvokers(String service, List<String> addressList){
        List<Invoker> invokers = new ArrayList<>();
        for(String address : addressList){
            String path = ZK_REGISTRY_PATH + "/" + service + "/providers" + "/" + address;//TODO 注册时权重
            int weight = zkClient.readData(path)==null?0:zkClient.readData(path);
            invokers.add(new Invoker(address, weight));
        }
        return invokers;
    }

    public ConcurrentHashMap<String, List<Invoker>> getServerMap() {
        return serverMap;
    }

    public List<Invoker> getServerList(String service) {
        if (serverMap.containsKey(service)) {
            return serverMap.get(service);
        }
        //找不到服务，返回空的服务区列表
        // TODO 找不到去刷新serverMap，然后在找。
        return new ArrayList<>();
    }


    private void createPersistent(String path) {
        if (!zkClient.exists(path)) {
            String dirs[] = path.substring(1).split("/");
            String tmp = "";
            for (String dir : dirs) {
                tmp += "/" + dir;
                if (!zkClient.exists(tmp)) {
                    try {
                        zkClient.createPersistent(tmp);
                    } catch (RuntimeException e) {
                        log.error("zookeeper创建结点错误");
                        e.printStackTrace();
                    }
                }
            }
        }


    }


//    private void watchNode(){
//        List<String> services = zkClient.subscribeChildChanges(ZK_REGISTRY_PATH, new IZkChildListener() {
//            @Override
//            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
//                log.info("检测到服务节点发生变化");
//                serviceList.clear();
//                for(String node : currentChilds){
//                    serviceList.add(node);
//                }
//                log.info("已发现服务节点列表为：" + serviceList);
//            }
//        });
//        for(String node : services){
//            serviceList.add(node);
//        }
//        log.info("已发现服务节点列表为：" + serviceList);
//
//    }


}

