/* Initial beliefs and rules */
safezone(drone1).
/* Plans */



/* 1v1 version */

	
// Drone in safezone: move_towards drone1
+safezone(drone1)
: not health(0) & not charge(0)
	<- move_towards(drone3);
	?safezone(drone1).
	
+not_safezone(drone1)
: not health(0) & not charge(0)
	<- flee(drone1);
	?not_safezone(drone1).


/* 2v2 version */

/*  Send drone2 information about health
+update_status(drone3,x)
: true
<- .send(drone3,tell,health(x)).
*/


// Drone in safezone: move_towards drone1
+safezone(drone1,drone3)
	: not health(0) & not charge(0)
	<- move_towards(drone3);
	?safezone(drone1,drone3).
	
+safezone(drone1,drone4)
	: not health(0) & not charge(0)
	<- move_towards(drone4);
	?safezone(drone1,drone4).
	
	
+not_safezone(drone1,drone3)
	: not health(0) & not charge(0)
	<- flee(drone1);
	?not_safezone(drone1,drone3).
	
+not_safezone(drone1,drone4)
	: not health(0) & not charge(0)
	<- flee(drone1);
	?not_safezone(drone1,drone4).
	
+low_ammo(drone1)
	: not health(0) & not charge(0)
	<- .send(drone2,tell,low_ammo(drone1)); 
	seek(ammo).
	
	
+low_health(drone1)
	: not health(0) & not charge(0)
	<- .send(drone2,tell,low_health(drone1)); 
	seek(health).
	
	
+low_charge(drone1)
	: not health(0) & not charge(0)
	<- .send(drone2,tell,low_charge(drone1)); 
	seek(charge).
	
+health(X) 
: X = 0
	<- .send(drone2,tell,dead(drone1)). 



