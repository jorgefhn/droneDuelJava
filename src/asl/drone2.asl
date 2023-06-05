/* Initial beliefs and rules */
safezone(drone2).
/* Plans */
	
/* El dron quiere calcular la nueva posición. Lo hará en base a varias prioridades:
1) no se encuentra en la zona segura 
2) tiene sus barras de carga, salud y munición vacías
3) está perfecto y va a calcular una nueva posición */

// Drone in safezone: move_towards drone2
+safezone(drone2) 
	: not health(0) & not charge(0)
	<- move_towards(drone1);
	?safezone(drone2).
	
	
+not_safezone(drone2)
	: not health(0) & not charge(0)
	<- flee(drone2);
	?not_safezone(drone2).
	



/*Resto de casos: el dron está en la zona segura

// 2.1) no tiene batería, va a por ella
+new_position(drone2)
	: safezone(drone2)& charge(0)
	<- findbat(drone2).

// 2.2) no tiene salud, va a por ella
+new_position(drone2)
	: safezone(drone2)& health(20)
	<- findhealth(drone2).

// 2.3) no tiene munición, va a por ella
+new_position(drone2)
	: safezone(drone2)& ammo(0)
	<- findammo(drone2).
*/

	

