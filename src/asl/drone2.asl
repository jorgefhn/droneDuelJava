/* Initial beliefs and rules */
safezone(drone2).
/* Plans */





/*1v1 version */
	
// Drone in safezone: move_towards drone1
+safezone(drone2)
: not health(0) & not charge(0)
	<- move_towards(drone4);
	?safezone(drone2).
	
+not_safezone(drone2)
: not health(0) & not charge(0)
	<- flee(drone2);
	?not_safezone(drone2).

/*2v2 version */
/*  Send drone2 information about health
+update_status(drone3,x)
: true
<- .send(drone3,tell,health(x)).
*/


// Drone in safezone
+safezone(drone2,drone3)
	: not health(0) & not charge(0)
	<- move_towards(drone3);
	?safezone(drone2,drone3).
	
+safezone(drone2,drone4)
	: not health(0) & not charge(0)
	<- move_towards(drone4);
	?safezone(drone2,drone4).
	
	
+not_safezone(drone2,drone3)
	: not health(0) & not charge(0)
	<- flee(drone2);
	?not_safezone(drone2,drone3).
	
+not_safezone(drone2,drone4)
	: not health(0) & not charge(0)
	<- flee(drone2);
	?not_safezone(drone2,drone4).
	

+low_ammo(drone2)
	: not health(0) & not charge(0)
	<- .send(drone1,tell,low_ammo(drone2)); 
		seek(ammo).
	
	
+low_health(drone2)
	: not health(0) & not charge(0)
	<- .send(drone1,tell,low_health(drone2)); 
	seek(health).
	
	
+low_charge(drone2)
	: not health(0) & not charge(0)
	<- .send(drone1,tell,low_charge(drone2)); 
	seek(charge).
	
	
+health(X) 
: X = 0
	<- .send(drone1,tell,dead(drone2)). 
	