package com.zanebono.chart.WebSocket;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * 获取HttpSession的配置类
 * 只要在websocket类上@ServerEndpoint注解中加入configurator=GetHttpSessionConfigurator.class
 * 再在 @OnOpen注解的方法里加入参EndpointConfig config
 * 通过config得到httpSession
 */
@ServerEndpoint(value="/websocket" ,configurator=GetHttpSessionConfigurator.class)//得到httpSession
@Component
public class WebSocketChat {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketChat> webSocketSet = new CopyOnWriteArraySet<WebSocketChat>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //当前会话的httpession
    private HttpSession httpSession;

    //因为HttpSession生命周期，WebSocket是长连接，会出现用户名丢失的情况，因此将用户名保存到WebSocket对象中
    private String username;
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param config   有
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        //得到httpSession
        this.httpSession = (HttpSession) config.getUserProperties()
                .get(HttpSession.class.getName());
        this.session = session;

        if(this.httpSession.getAttribute("username")==null||this.httpSession.getAttribute("username")==""){
            this.httpSession.setAttribute("username","");
            this.activeOnClose("非法登入");
        }
        else {
            //新增的登陆冲突判断
            this.JudgeOnline(httpSession);
            webSocketSet.add(this);     //加入set中
            this.username=(String) this.httpSession.getAttribute("username");//保存用户名
        }
    }

    /**
     * 连接关闭调用的方法
     */
    //客户端主动断开
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    //因为重复登陆的服务端主动断开
    public void activeOnClose(String description){
        //尝试关闭连接
        try {
            this.sendMessage(description);
            this.session.close();//会触发上面的onclose注解方法
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("session关闭！原因:"+description);
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //获得消息发来时间
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sd.format(new Date());

        System.out.println("来自客户端的消息:" + message);
//        //替换表情
//        message = replaceImage(message);
        //得到当前用户名
//        String name = (String) this.httpSession.getAttribute("username");
//        if(name==null)
//            this.activeOnClose("用户名丢失");//缺少用户名的断开
    //因为将username保存到了对象中，因此放弃了HttpSession
        //群发消息
        for(WebSocketChat item: webSocketSet){
            try {
                item.sendMessage(this.username+" "+time+"</br>"+message);
            } catch (IOException e) {
                e.printStackTrace(); continue;
            }
        }
    }
    /** * 发生错误时调用
     * @param session
     * @param error
     * @OnError
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /** * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
    public void sendMessage(String message,String userid) throws IOException{ }
    //对count的加减获取同步
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketChat.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketChat.onlineCount--;
    }
//    //替换表情
//    private String replaceImage(String message){
//        for(int i=1;i<11;i++){
//            if(message.contains("<:"+i+":>")){
//                message = message.replace("<:"+i+":>", "<img " + "src='/chat/Images/" + i + ".gif' id='" + i + "' />");
//            }
//        }
//        return message;
//    }
    //登陆冲突处理
    public static void JudgeOnline(HttpSession httpSession){
        for(WebSocketChat webSocketChat:WebSocketChat.webSocketSet){
//            if(webSocketChat.httpSession.getAttribute("username").equals(httpSession.getAttribute("username"))){
//                webSocketChat.httpSession.setAttribute("username","");
//                webSocketChat.activeOnClose("重复登录");
//            }
            if(webSocketChat.username.equals(httpSession.getAttribute("username"))){
                webSocketChat.httpSession.setAttribute("username","");
                webSocketChat.activeOnClose("重复登录");
            }
        }
    }
}