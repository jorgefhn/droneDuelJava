package agents2v2;
 
import javax.json.JsonArray;

/** class that implements the Model of Domestic Robot application */
public class MapModel {

     
   Point3D getPackagePos(JsonArray packageList)
   {
	   System.out.println(packageList);
	   System.out.println(packageList.get(0));

	   Point3D dest = new Point3D(0.0,0.0,0.0);
	   
	   return dest;
	   
   }
	 
	Point3D getSafePositiond1(int randomd1){
		Point3D destinyd1 = new Point3D(974.0,122.0,0.0);
	
		if (randomd1 == 1) {
			destinyd1.setZ(-454.0);
		}
		if (randomd1 == 2) {		
			destinyd1.setZ(992.0);
		}
	
		if (randomd1 == 3) {
			destinyd1.setZ(206.0);
		}
		
		return destinyd1;
		
	}
	
	Point3D getSafePositiond2(int randomd2){
		Point3D destinyd2 = new Point3D(-494.1,100.0,0.0);

	
		if (randomd2 == 1) {
			destinyd2.setZ(994.7);
		}
		if (randomd2 == 2) {		
			destinyd2.setZ(-502.0);
		}
	
		if (randomd2 == 3) {
			destinyd2.setZ(228.0);
		}
		return destinyd2;
		
	}
	
	Point3D target(Point3D dpos){
		Point3D destiny = new Point3D(0.0,0.0,0.0);
	
		destiny.setX(dpos.getX());
		destiny.setY(dpos.getY());
		destiny.setZ(dpos.getZ());
			
	
		return destiny;
		
	}
	

	
	
	
}
    

    


   

    
