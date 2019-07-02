package com.zanebono.chart.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/WebRtc")
public class WebRtcController {
    @RequestMapping("/test")
    public ModelAndView getHtml(){
      ModelAndView modelAndView = new ModelAndView("/websocket_webrtc");
      return modelAndView;
    }
}
