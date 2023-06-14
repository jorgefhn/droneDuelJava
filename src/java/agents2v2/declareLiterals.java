package agents2v2;

import jason.asSyntax.Literal;

public interface declareLiterals {

	// Drone 1 percepts

	// Safezone
	public static final Literal sz13 = Literal.parseLiteral("safezone(drone1,drone3)");
	public static final Literal sz14 = Literal.parseLiteral("safezone(drone1,drone4)");
	public static final Literal sz23 = Literal.parseLiteral("safezone(drone2,drone3)");
	public static final Literal sz24 = Literal.parseLiteral("safezone(drone2,drone4)");

	public static final Literal nsz13 = Literal.parseLiteral("not_safezone(drone1,drone3)");
	public static final Literal nsz14 = Literal.parseLiteral("not_safezone(drone1,drone4)");
	public static final Literal nsz23 = Literal.parseLiteral("not_safezone(drone2,drone3)");
	public static final Literal nsz24 = Literal.parseLiteral("not_safezone(drone2,drone4)");

	// Drone's plans: they decide their new positions
	public static final Literal np1 = Literal.parseLiteral("new_position(drone1)");
	public static final Literal np2 = Literal.parseLiteral("new_position(drone2)");
	public static final Literal np3 = Literal.parseLiteral("new_position(drone3)");
	public static final Literal np4 = Literal.parseLiteral("new_position(drone4)");

	// Drones seek for health, charge and ammo
	public static final Literal sh1 = Literal.parseLiteral("seek_health(drone1)");
	public static final Literal sa1 = Literal.parseLiteral("seek_charge(drone1)");
	public static final Literal sc1 = Literal.parseLiteral("seek_ammo(drone1)");

	public static final Literal sh2 = Literal.parseLiteral("seek_health(drone2)");
	public static final Literal sa2 = Literal.parseLiteral("seek_charge(drone2)");
	public static final Literal sc2 = Literal.parseLiteral("seek_ammo(drone2)");

	public static final Literal sh3 = Literal.parseLiteral("seek_health(drone3)");
	public static final Literal sa3 = Literal.parseLiteral("seek_charge(drone3)");
	public static final Literal sc3 = Literal.parseLiteral("seek_ammo(drone3)");

	public static final Literal sh4 = Literal.parseLiteral("seek_health(drone2)");
	public static final Literal sa4 = Literal.parseLiteral("seek_charge(drone2)");
	public static final Literal sc4 = Literal.parseLiteral("seek_ammo(drone2)");
	
	
	



}