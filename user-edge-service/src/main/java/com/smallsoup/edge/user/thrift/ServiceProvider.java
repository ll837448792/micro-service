package com.smallsoup.edge.user.thrift;

import com.smallsoup.thrift.message.MessageService;
import com.smallsoup.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: micro-service
 * @description: UserServiceProvider
 * @author: smallsoup
 * @create: 2018-09-15 14:59
 **/
@Component
public class ServiceProvider {

    @Value("${thrift.user.port}")
    private int userServicePort;

    @Value("${thrift.user.ip}")
    private String userServiceName;

    @Value("${thrift.message.port}")
    private int messageServicePort;

    @Value("${thrift.message.ip}")
    private String messageServiceName;

    @Value("${thrift.service.timeout}")
    private int timeout;


    public enum ServiceType {
        USER,
        MESSAGE
    }

    public UserService.Client getUserService() {
        return getService(userServiceName, userServicePort, ServiceType.USER);
    }

    public MessageService.Client getMessageService() {
        return getService(messageServiceName, messageServicePort, ServiceType.MESSAGE);
    }

    public <T> T getService(String serverHost, int serverPort, ServiceType type) {

        TSocket socket = new TSocket(serverHost, serverPort, timeout);

        TTransport transport = new TFramedTransport(socket);

        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }

        TProtocol protocol = new TBinaryProtocol(transport);


        TServiceClient client = null;
        switch (type) {
            case USER:
                client = new UserService.Client(protocol);
                break;
            case MESSAGE:
                client = new MessageService.Client(protocol);
                break;
        }

        return (T)client;
    }
}
