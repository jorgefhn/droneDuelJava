/* Initial beliefs and rules */
safezone(drone4).
/* Plans */
	
/* El dron quiere calcular la nueva posición. Lo hará en base a varias prioridades:
1) no se encuentra en la zona segura 
2) tiene sus barras de carga, salud y munición vacías
3) está perfecto y va a calcular una nueva posición */

// Drone in safezone
+safezone(drone1,drone4)
	: not health(0) & not charge(0)
	<- move_towards(drone1);
	?safezone(drone1,drone4).
	
+safezone(drone2,drone4)
	: not health(0) & not charge(0)
	<- move_towards(drone2);
	?safezone(drone2,drone4).
	
	
+not_safezone(drone1,drone4)
	: not health(0) & not charge(0)
	<- flee(drone4);
	?not_safezone(drone1,drone4).
	
+not_safezone(drone2,drone4)
	: not health(0) & not charge(0)
	<- flee(drone4);
	?not_safezone(drone2,drone4).
	
+low_ammo(drone4)
	: not health(0) & not charge(0)
	<- .send(drone3,tell,low_ammo(drone4)); 
	seek(ammo).
	
	
+low_health(drone4)
	: not health(0) & not charge(0)
	<- .send(drone3,tell,low_health(drone4)); 
	seek(health).
	
	
+low_charge(drone4)
	: not health(0) & not charge(0)
	<- .send(drone3,tell,low_charge(drone4)); 
	seek(charge).
	
+dead(drone4)[source(drone3)]
:true
<- concern(drone4).	


+health(X) 
: X = 0
	<- .send(drone3,tell,dead(drone4)). 



