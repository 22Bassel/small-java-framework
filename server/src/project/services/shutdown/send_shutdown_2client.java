package project.services.shutdown;

import project.DAO.User_Dao;
import project.errors.Error;
import project.requests.Request;
import project.services.Service;
import project.services.servicemessages.ServiceMessage;
import project.services.servicemessages.String_ServiceMessage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class send_shutdown_2client extends Service {
    public send_shutdown_2client() {
        super("send_shutdown_2client");
    }

    @Override
    public ServiceMessage execute(Map parameters) throws IOException, SQLException, ClassNotFoundException {
        servicemessage=intalize_servicemessage();
        ArrayList<ArrayList<String>> userDB2=new User_Dao().FindByUserID((String) parameters.get("SecondUserID"));

        if( userDB2.size()==1 ){
            servicemessage.add("error", Error.NotFound.toString());
            return servicemessage;
        }

        Map object=make_map();


        object=add_map_to_map(object,parameters);

        object.put("service_name","DoShutDown");

        Request request=make_request("json", object) ;

        try {

            ServiceMessage serviceMessage  = send_request("socket", (String) parameters.get("SecondUserID"), request);

            if (serviceMessage.get_result().containsKey("error")) {

                servicemessage.add("error", "client refused");

            } else {
                servicemessage.add("msg", "true");
            }

        }catch (IOException e){

            System.out.println("user 2 gone  ");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return servicemessage;

    }

    @Override
    public ServiceMessage intalize_servicemessage() {
        return new String_ServiceMessage();
    }


}
