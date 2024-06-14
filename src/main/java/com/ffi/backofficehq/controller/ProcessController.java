package com.ffi.backofficehq.controller;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.service.ProcessService;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = "/api/sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send Message", description = "Send Message")
    public @ResponseBody
    ResponseMessage sendMessage(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("param: " + param);

        String topic = balance.get("topic");
        String message = balance.get("message");
        simpMessagingTemplate.convertAndSend("/topic/outlet/0208", message);
        ResponseMessage rm = new ResponseMessage();
        rm.setSuccess(true);
        return rm;
    }

    @MessageMapping("/send-message") // Endpoint where the client sends messages
    @SendTo("/topic/outlet") // Topic to which the server sends messages
    public String handleMessage(String message) {
        System.out.println("Received message from client: " + message);
        // Process the message here if needed
        return "Message received: " + message;
    }

    // @MessageMapping("/sendMessage")
    // @SendTo("/topic/receivedMessage")
    // public String handleMessage(String message) {
    //     return " Message received: " + message;
    // }
    // @MessageMapping("/sendMessage2")
    // @SendTo("/topic")
    // public String handleMessageTopic(String message) {
    //     return " Message received: " + message;
    // }
    @MessageMapping("/sendMessage")
    @SendTo("/topic/{topic}")
    public String handleMessageOutlet(@Payload String message, @DestinationVariable String topic) {
        // to do continue //
        return " Message received: " + message;
    }

    @PostMapping(path = "/api/branch/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert branch", description = "Insert master branch")
    public @ResponseBody
    ResponseMessage insertBranch(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertBranch(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/branch/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update branch", description = "Update master branch")
    public @ResponseBody
    ResponseMessage updateBranch(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateBranch(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/area/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert area", description = "Insert master area")
    public @ResponseBody
    ResponseMessage insertArea(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertArea(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/area/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update area", description = "Update master area")
    public @ResponseBody
    ResponseMessage updateArea(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateArea(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/city/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert city", description = "Insert master city")
    public @ResponseBody
    ResponseMessage insertCity(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertCity(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/city/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update city", description = "Update master city")
    public @ResponseBody
    ResponseMessage updateCity(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateCity(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/location/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert location", description = "Insert master location")
    public @ResponseBody
    ResponseMessage insertLocation(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertLocation(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/location/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update location", description = "Update master location")
    public @ResponseBody
    ResponseMessage updateLocation(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateLocation(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/uom/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert UOM", description = "Insert master UOM")
    public @ResponseBody
    ResponseMessage insertUom(User user, @RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertUom(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/uom/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update UOM", description = "Update master UOM")
    public @ResponseBody
    ResponseMessage updateUom(User user, @RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateUom(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/rsc/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert RSC", description = "Insert master RSC")
    public @ResponseBody
    ResponseMessage insertRsc(User user, @RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertRsc(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/rsc/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update RSC", description = "Update master RSC")
    public @ResponseBody
    ResponseMessage updateRsc(User user, @RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateRsc(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/region/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert region", description = "Insert master region")
    public @ResponseBody
    ResponseMessage insertRegion(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertRegion(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/region/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update region", description = "Update master region")
    public @ResponseBody
    ResponseMessage updateRegion(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateRegion(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/supplier/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert supplier", description = "Insert master supplier")
    public @ResponseBody
    ResponseMessage insertSupplier(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/supplier/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update supplier", description = "Update master supplier")
    public @ResponseBody
    ResponseMessage updateSupplier(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateSupplier(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/product/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert product", description = "Insert master product")
    public @ResponseBody
    ResponseMessage insertProduct(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.insertProduct(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/product/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update product", description = "Update master product")
    public @ResponseBody
    ResponseMessage updateProduct(User user, @RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResponseMessage rm = new ResponseMessage();
        try {
            processService.updateProduct(balance);
            rm.setSuccess(true);
            rm.setMessage("Update Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }
}
