package com.chason.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chason.system.service.SessionService;

@Controller
public class WebSocketController {
	@Autowired
	SimpMessagingTemplate template;

	@Autowired
    SessionService sessionService;

	/*@Autowired
	WelcomeTask welcomeTask;

	@MessageMapping("/welcome") // 浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
	@SendTo("/topic/getResponse") // 服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
	public Response say(Message message) throws Exception {
		Thread.sleep(1000);
		return new Response("Welcome, " + message.getName() + "!");
	}

	@GetMapping("/test")
	String test() {
		return "test";
	}

	@RequestMapping("/welcome")
	@ResponseBody
	public R say02() {
		try {
			welcomeTask.sayWelcome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return R.ok();
	}*/
//    @ResponseBody
//    @GetMapping("/chat")
//    public String  handleChat(Principal principal, String msg) {
//        template.convertAndSendToUser(sessionService.listPrincipal().get(0).toString(), "/queue/notifications", principal.getName() + "给您发来了消息：" + msg);
//        return sessionService.listPrincipal().get(0).toString();
//    }
}
