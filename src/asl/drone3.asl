/* Only 2v2 version */

/* Initial beliefs and rules */
safezone(drone3).
/* Plans */
	
/* El dron quiere calcular la nueva posición. Lo hará en base a varias prioridades:
1) no se encuentra en la zona segura 
2) tiene sus barras de carga, salud y munición vacías
3) está perfecto y va a calcular una nueva posición */

/*  Send drone2 information about health
+update_status(drone3,x)
: true
<- .send(drone3,tell,health(x)).
*/




// Drone in safezone
+safezone(drone1,drone3)
	: not health(0) & not charge(0)
	<- move_towards(drone1);
	?safezone(drone1,drone3).
	
+safezone(drone2,drone3)
	: not health(0) & not charge(0)
	<- move_towards(drone2);
	?safezone(drone2,drone3).
	

	
+not_safezone(drone1,drone3)
	: not health(0) & not charge(0)
	<- flee(drone3);
	?not_safezone(drone1,drone3).
	
+not_safezone(drone2,drone3)
	: not health(0) & not charge(0)
	<- flee(drone3);
	?not_safezone(drone2,drone3).
	
	
+low_ammo(drone3)
	: not health(0) & not charge(0)
	<- .send(drone4,tell,low_ammo(drone3)); 
	seek(ammo).
	
	
+low_health(drone3)
	: not health(0) & not charge(0)
	<- .send(drone4,tell,low_health(drone3)); 
	seek(health).
	
	
+low_charge(drone3)
	: not health(0) & not charge(0)
	<- .send(drone4,tell,low_charge(drone3)); 
	seek(charge).

+health(X) 
: X = 0
	<- .send(drone4,tell,dead(drone3)). 
