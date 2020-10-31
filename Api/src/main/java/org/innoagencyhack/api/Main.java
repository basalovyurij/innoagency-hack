package org.innoagencyhack.api;

import com.google.gson.Gson;
import io.jooby.Jooby;
import static io.jooby.Jooby.runApp;
import io.jooby.MediaType;
import org.innoagencyhack.api.controllers.RawTextController;

/**
 *
 * @author yurij
 */
public class Main extends Jooby {
    
    {
        Gson lib = new Gson();           

        encoder(MediaType.json, (ctx, result) -> {      
          String json = lib.toJson(result);              
          ctx.setDefaultResponseType(MediaType.json);    
          return json.getBytes();                                   
        });
        
        //get("/", ctx -> "Welcome to Jooby!");
  
        mvc(new RawTextController());
    }
    
    public static void main(String[] args) {
        runApp(args, Main::new);
    }
}
