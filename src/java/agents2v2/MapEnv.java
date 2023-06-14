package agents2v2;




import jason.environment.Environment;
import java.util.logging.Logger;
import java.util.Random;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.lang.InterruptedException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


import java.io.StringReader;


public class MapEnv extends Environment implements declareLiterals {
    

	gameInfo game = new gameInfo();
	
	// prueba
	
	// destinies 
	public destiniesBuffer destinies = new destiniesBuffer();
	
	
	static Logger logger = Logger.getLogger(MapEnv.class.getName());
	MapModel model; // the model of the grid
		
	
	public class Sender extends Thread{
		// Sender method: it sends periodically the destinies of the drones
		@Override
        public void run(){
					
			DatagramSocket mySocket = null;
			try {
				
				mySocket = new DatagramSocket();
				InetAddress host = InetAddress.getByName("127.0.0.1"); 
				int port = 11000; 
				
				while ( true ) {
					String destiniesString = destinies.toString();		
					System.out.println("I am going to send: "+destiniesString);
					byte[] bytes = destiniesString.getBytes();	
					DatagramPacket packet = new DatagramPacket(bytes, destiniesString.length(),host,port); 
					mySocket.send(packet);
					Thread.sleep(1000);
				}
				
				
				
			} catch(SocketException e){
				e.printStackTrace();			
			} catch(UnknownHostException e){
				e.printStackTrace();			
			} catch(IOException e){
				e.printStackTrace();			
			} catch(InterruptedException e){
				e.printStackTrace();			
			}
			
			mySocket.close();
			

		
		}
		
	}
	

    public class Listener extends Thread{
    	// Listener method: it periodically receives a JSON with all the information 
	
		
        @Override
        public void run(){
        	
			DatagramSocket mySocket = null;

            try {
            	
            	mySocket = new DatagramSocket(11004);
                byte[] buffer = new byte[1024];
                

                // Update drones info
                while ( true ) {
						
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    mySocket.receive(peticion);
                    String mensaje = new String(peticion.getData(),0,peticion.getLength());
                    System.out.println("Recibido: "+mensaje);
                    
                    updateFromUnity(mensaje);
					updatePercepts();
	                Thread.sleep(1000);

                }
                
            } catch(SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            mySocket.close();    

        
        }
        

		private void updateFromUnity(String mensaje) {
			// method to update positions with a message received from Unity
			
			
			JsonReader jsonReader = Json.createReader(new StringReader(mensaje));
			JsonObject newLocations= jsonReader.readObject();
			jsonReader.close();
			
			JsonObject drone1 = newLocations.getJsonObject("drone1");
			JsonObject drone2 = newLocations.getJsonObject("drone2");
			JsonObject drone3 = newLocations.getJsonObject("drone3");
			JsonObject drone4 = newLocations.getJsonObject("drone4");
			
			
			game.drone1.setPosition(drone1.getString("position"));
			game.drone2.setPosition(drone2.getString("position"));
			game.drone3.setPosition(drone3.getString("position"));
			game.drone4.setPosition(drone4.getString("position"));
			
			
			// obtenemos indicadores de salud, carga y munición de drones
		
			game.drone1.setHealthLevel(drone1.get("health"));
			game.drone1.setChargeLevel(drone1.get("charge"));
			game.drone1.setAmmoLevel(drone1.get("ammo"));

			game.drone2.setHealthLevel(drone2.get("health"));
			game.drone2.setChargeLevel(drone2.get("charge"));
			game.drone2.setAmmoLevel(drone2.get("ammo"));
			
			game.drone3.setHealthLevel(drone3.get("health"));
			game.drone3.setChargeLevel(drone3.get("charge"));
			game.drone3.setAmmoLevel(drone3.get("ammo"));

			game.drone4.setHealthLevel(drone4.get("health"));
			game.drone4.setChargeLevel(drone4.get("charge"));
			game.drone4.setAmmoLevel(drone4.get("ammo"));

			// packages
			game.updateHealthPackages(newLocations.getJsonArray("healthPackages"));
			game.updateChargePackages(newLocations.getJsonArray("chargePackages"));
			game.updateAmmoPackages(newLocations.getJsonArray("ammoPackages"));
			
			

    }
    }
   
	
  

    @Override
    public void init(String[] args) {
    	
    	game.drone1 = new droneInfo();
    	game.drone2 = new droneInfo();
    	game.drone3 = new droneInfo();
    	game.drone4 = new droneInfo();
    	
    	Point3D origin = new Point3D(0.0,0.0,0.0);
    	game.drone1.setPosition(origin.toString()); // default
    	game.drone2.setPosition(origin.toString()); // default
    	game.drone3.setPosition(origin.toString()); // default
    	game.drone4.setPosition(origin.toString()); // default

    	
    	// by default, drones will move towards the enemy

    	destinies.setTarget("drone1",arrayToPoint3D("drone3"));
        destinies.setTarget("drone2",arrayToPoint3D("drone4"));
        destinies.setTarget("drone3",arrayToPoint3D("drone1"));
        destinies.setTarget("drone4",arrayToPoint3D("drone2"));
    	
    		
        model = new MapModel();
        
        // First, listener 
		Listener listener = new Listener();
		listener.start();
		
		 
		Sender sender = new Sender();
		sender.start();

        


    }

	

    /** creates the agents percepts based on the MapModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("drone1");
        clearPercepts("drone2");
        clearPercepts("drone3");
        clearPercepts("drone4");
        
        //Add percepts of charge, battery an ammo to both drones
        String h1string = "health("+game.drone1.health+")";
        String c1string = "charge("+game.drone1.charge+")";
        String a1string = "ammo("+game.drone1.ammo+")";

        String h2string = "health("+game.drone2.health+")";
        String c2string = "charge("+game.drone2.charge+")";
        String a2string = "ammo("+game.drone2.ammo+")";
        
        String h3string = "health("+game.drone3.health+")";
        String c3string = "charge("+game.drone3.charge+")";
        String a3string = "ammo("+game.drone3.ammo+")";
        
        String h4string = "health("+game.drone4.health+")";
        String c4string = "charge("+game.drone4.charge+")";
        String a4string = "ammo("+game.drone4.ammo+")";
        
    	Literal h1 = Literal.parseLiteral(h1string);
    	Literal c1 = Literal.parseLiteral(c1string);
    	Literal a1 = Literal.parseLiteral(a1string);
    	
     	Literal h2 = Literal.parseLiteral(h2string);
    	Literal c2 = Literal.parseLiteral(c2string);
    	Literal a2 = Literal.parseLiteral(a2string);
    	
    	Literal h3 = Literal.parseLiteral(h3string);
    	Literal c3 = Literal.parseLiteral(c3string);
    	Literal a3 = Literal.parseLiteral(a3string);
    	
     	Literal h4 = Literal.parseLiteral(h4string);
    	Literal c4 = Literal.parseLiteral(c4string);
    	Literal a4 = Literal.parseLiteral(a4string);
    	
    	// Update status
    	// Update status
    	String status1 = "updatestatus(drone1,"+game.drone1.health+")";
    	String status2 = "updatestatus(drone2,"+game.drone2.health+")";
    	String status3 = "updatestatus(drone3,"+game.drone3.health+")";
    	String status4 = "updatestatus(drone4,"+game.drone4.health+")";

    	
    	Literal us1= Literal.parseLiteral(status1);
    	Literal us2= Literal.parseLiteral(status2);
    	Literal us3= Literal.parseLiteral(status3);
    	Literal us4= Literal.parseLiteral(status4);

    	// drone1 and drone2 locations
        Point3D d1pos = arrayToPoint3D("drone1");
        Point3D d2pos = arrayToPoint3D("drone2");
        Point3D d3pos = arrayToPoint3D("drone3");
        Point3D d4pos = arrayToPoint3D("drone4");
        
        
        // After calculating the security distance, we will set a threshold of 50 to add a percept
		double securityDistance13 = d1pos.distanceBetweenVectors(d3pos);
		double securityDistance14 = d1pos.distanceBetweenVectors(d4pos);
		double securityDistance23 = d2pos.distanceBetweenVectors(d3pos);
		double securityDistance24 = d2pos.distanceBetweenVectors(d4pos);

       
        addPercept("drone1",h1);
        addPercept("drone1",c1);
        addPercept("drone1",a1);
       
        if (game.drone1.health > 0.0) {
        	// addPercept("drone1",us1);
        	if (game.drone1.health < 20.0) {
    			addPercept("drone1", sh1);	
            }
        	
        	if (game.drone1.charge < 20.0) {
    			addPercept("drone1", sc1);	
            }
            
            if (game.drone1.ammo < 20.0) {
    			addPercept("drone1", sa1);	
            }
            
            if (game.drone3.health > 0.0) {
            	if (securityDistance13 > 200.0){ // if the security distance is over 200, safezone.
        			addPercept("drone1", sz13);	
        			
        		} 
            	if (securityDistance13 <= 200.0) {
        			addPercept("drone1", nsz13);	

        		}
            	
            }
            
            if (game.drone3.health == 0.0 &&  game.drone4.health > 0.0) {
            
	            if (securityDistance14 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone1", sz14);	
	    			
	    		} 
	            if (securityDistance14 <= 200.0) {
	    			addPercept("drone1", nsz14);	
	
	    		}
            }
            
            
        }

        addPercept("drone2",h2);
        addPercept("drone2",c2);
        addPercept("drone2",a2);
        
        if (game.drone2.health > 0.0) {
            // addPercept("drone2",us2);
            
            if (game.drone2.health < 20.0) {
    			addPercept("drone2", sh2);	
            }
            
            if (game.drone2.charge < 20.0) {
    			addPercept("drone2", sc2);	
            }
            
            if (game.drone2.ammo < 20.0) {
    			addPercept("drone2", sa2);	
            }
            
            System.out.println("Drone 3 Health "+game.drone3.health);
            
            if (game.drone4.health > 0.0) {

            
	            if (securityDistance24 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone2", sz24);	
	    			
	    		} 
	            
	            if (securityDistance24 <= 200.0) {
	    			addPercept("drone2", nsz24);	
	
	    		}
	            
            }
            
            if (game.drone4.health == 0.0 && game.drone3.health > 0.0) {

            
	            if (securityDistance23 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone2", sz23);	
	    			
	    		} 
	            if (securityDistance23 <= 200.0) {
	    			addPercept("drone2", nsz23);	
	
	    		}
            }
            
        }
            

            
        addPercept("drone3",h3);
        addPercept("drone3",c3);
        addPercept("drone3",a3);
        
        if (game.drone3.health > 0.0) {
            // addPercept("drone3",us3);
            

            if (game.drone3.health < 20.0) {
    			addPercept("drone3", sh3);	
            }
            
            if (game.drone3.charge < 20.0) {
    			addPercept("drone3", sc3);	
            }
            
            if (game.drone3.ammo < 20.0) {
    			addPercept("drone3", sa3);	
            }
            
            
            if (game.drone1.health > 0.0) {

	            if (securityDistance13 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone3", sz13);	
	    			
	    		} 
	            if (securityDistance13 <= 200.0)  {
	    			addPercept("drone3", nsz13);	
	
	    		}
            }
            
            if (game.drone1.health == 0.0 &&  game.drone2.health > 0.0) {

	            if (securityDistance23 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone3", sz23);	
	    			
	    		} 
	            
	            if (securityDistance23 <= 200.0)  {
	    			addPercept("drone3", nsz23);	
	
	    		}
	            
            }
        }

        

        addPercept("drone4",h4);
        addPercept("drone4",c4);
        addPercept("drone4",a4);
        
        if (game.drone4.health > 0.0) {
            // addPercept("drone4",us4);
            
            if (game.drone4.health < 20) {
    			addPercept("drone4", sh4);	
            }
            
            if (game.drone4.charge < 20) {
    			addPercept("drone4", sc4);	
            }
            
            if (game.drone4.ammo < 20) {
    			addPercept("drone4", sa4);	
            }
            
            if (game.drone2.health > 0.0) {

	            if (securityDistance24 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone4", sz24);	
	    			
	    		} 
	            if (securityDistance24 <= 200.0) {
	    			addPercept("drone4", nsz24);	
	
	    		}
            
            }
            
            if (game.drone2.health == 0.0 &&  game.drone1.health > 0.0) {

	            if (securityDistance14 > 200.0){ // if the security distance is over 200, safezone.
	    			addPercept("drone4", sz14);	
	    			
	    		} 
	            if (securityDistance14 <= 200.0) {
	    			addPercept("drone4", nsz14);	
	
	    		}
	            
            }
            
            
        }

		System.out.println("[drone1]: "+consultPercepts("drone1"));
		System.out.println("[drone2]: "+consultPercepts("drone2"));
		System.out.println("[drone3]: "+consultPercepts("drone3"));
		System.out.println("[drone4]: "+consultPercepts("drone4"));

		
		

		  
    }

  
    @Override
    public boolean executeAction(String ag, Structure action ) {
    	
    	System.out.println("["+ag+"] doing "+action);
    	// drone1 and drone2 locations
        Point3D d1pos = arrayToPoint3D("drone1");
        Point3D d2pos = arrayToPoint3D("drone2");
        Point3D d3pos = arrayToPoint3D("drone3");
        Point3D d4pos = arrayToPoint3D("drone4");
        
        
    	
    	boolean result = false;
    	
		if (action.getFunctor().equals("seek_health"))
		{
			String packString = Json.createValue(game.healthPackages.get(0).toString()).toString();
			Point3D pack = new Point3D(0.0,0.0,0.0);
			pack.toPoint3D(packString);
			Point3D dest =  model.target(pack);
			destinies.setTarget(ag, dest);
			result = true;
		}
		
		if (action.getFunctor().equals("concern")) {
			System.out.println("my partner is dead");
			result = true;
		}
		
		if (action.getFunctor().equals("seek_ammo"))
		{
			String packString = Json.createValue(game.ammoPackages.get(0).toString()).toString();
			Point3D pack = new Point3D(0.0,0.0,0.0);
			pack.toPoint3D(packString);
			Point3D dest =  model.target(pack);
			destinies.setTarget(ag, dest);	
			result = true;
		}
		
		if (action.getFunctor().equals("seek_charge"))
		{
			String packString = Json.createValue(game.chargePackages.get(0).toString()).toString();
			Point3D pack = new Point3D(0.0,0.0,0.0);
			pack.toPoint3D(packString);
			Point3D dest =  model.target(pack);
			destinies.setTarget(ag, dest);
			result = true;

		}
		
		if (action.getFunctor().equals("move_towards"))
		{
			if (ag.equals("drone1")) {
				
				Point3D dest1 = new Point3D(0.0,0.0,0.0);
				
				if (game.drone3.health > 0) { // target = drone3
					dest1 = model.target(d3pos);
				} else { // target = drone4
					dest1 = model.target(d4pos);					
				}
				
				destinies.setTarget("drone1", dest1);

				
				
						

			}
			
			if (ag.equals("drone2")) {
				Point3D dest2 = new Point3D(0.0,0.0,0.0);

				if (game.drone4.health > 0) { // target = drone3
					dest2 = model.target(d4pos);
				} else { // target = drone4
					dest2 = model.target(d3pos);					
				}
				destinies.setTarget("drone2", dest2);		

			}
			
			if (ag.equals("drone3")) {
				Point3D dest3 = new Point3D(0.0,0.0,0.0);

				if (game.drone1.health > 0) { // target = drone3
					dest3 = model.target(d1pos);
				} else { // target = drone4
					dest3 = model.target(d2pos);					
				}
				destinies.setTarget("drone3", dest3);		

			}
			
			if (ag.equals("drone4")) {
				Point3D dest4 = new Point3D(0.0,0.0,0.0);

				if (game.drone2.health > 0) { // target = drone3
					dest4 = model.target(d2pos);
				} else { // target = drone4
					dest4 = model.target(d1pos);					
				}
				destinies.setTarget("drone4", dest4);		

			}
			
			result = true;
		}
		

		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			// generate random for d1 and d2 
			Random random = new Random();

			

			if (ag.equals("drone1")) {
				int randomd1 = random.nextInt(3) + 1;
				Point3D dest1 = model.getSafePositiond1(randomd1);
				destinies.setTarget("drone1", dest1);	
			}
			
			if (ag.equals("drone2")) {
				int randomd2 = random.nextInt(3) + 1;
				Point3D dest2 = model.getSafePositiond1(randomd2);
				destinies.setTarget("drone2", dest2);	
			}
			
			if (ag.equals("drone3")) {
				int randomd3 = random.nextInt(3) + 1;
				Point3D dest3 = model.getSafePositiond2(randomd3);
				destinies.setTarget("drone3", dest3);	
			}
			
			if (ag.equals("drone4")) {
				int randomd4 = random.nextInt(3) + 1;
				Point3D dest4 = model.getSafePositiond2(randomd4);
				destinies.setTarget("drone4", dest4);	
			}
		
			result = true;			
		}
		
		
          
        if (result) {

            updatePercepts();
			informAgsEnvironmentChanged();
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        }
        
       
        return result;
    }



	



	public Point3D arrayToPoint3D(String droneName) {
		JsonArray posArray = null;

		
		if (droneName.equals("drone1")){
			posArray = game.drone1.position;

		}
		
		if (droneName.equals("drone2")){
			posArray = game.drone2.position;

		}
		
		if (droneName.equals("drone3")){
			posArray = game.drone3.position;

		}
		
		if (droneName.equals("drone4")){
			posArray = game.drone4.position;

		}
		

		// JsonArray --> String --> Point3D
		Point3D vector = new Point3D(0.0,0.0,0.0);
		vector.toPoint3D(posArray.toString());
		return vector;
	} 
    
	 
	
}
    

