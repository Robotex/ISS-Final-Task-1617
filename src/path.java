


import it.unibo.qactors.akka.QActor;
import java.util.Stack;

import alice.tuprolog.Int;

import java.util.Queue;
import java.util.LinkedList;

public class path {
	
	public path (QActor myActor) {
		
	}

	public static class MovementAction {
		public MovementAction(String direction, int speed, int duration, int angle) {
			this.m_direction = direction;
			this.m_speed = speed;
			this.m_duration = duration;
			this.m_angle = angle;
		}
		String m_direction;
		int m_speed;
		int m_duration;
		int m_angle;
	}
	
	static Stack<MovementAction> stack = new Stack<MovementAction>();
	static Queue<MovementAction> commandQueue = new LinkedList<MovementAction>();
	
	public static void moveRover(QActor qa, String direction, String speed, String duration) {
		qa.println("moveRover(" + direction + ", " + speed + ", " + duration + ")");
		MovementAction movement = new MovementAction(direction, Integer.parseInt(speed), Integer.parseInt(duration), 0);
		stack.push(movement);
		qa.execUnity("rover", direction, Integer.parseInt(duration), Integer.parseInt(speed), 0);
	}
	
	public static void returnRover(QActor qa) {
		while (!stack.isEmpty()) {
			MovementAction lastMove = stack.pop();
			MovementAction reversedMove;
			
			String direction = lastMove.m_direction;
			if (direction == "left")
				reversedMove = new MovementAction("right", lastMove.m_speed, lastMove.m_duration, 0);
			else if (direction == "right")
				reversedMove = new MovementAction("left", lastMove.m_speed, lastMove.m_duration, 0);
			else
				reversedMove = lastMove;
			
			
			commandQueue.add(reversedMove);
			// feed
		
		}
	}

	public static void asdasdsa(QActor myself) {
		// TODO Auto-generated method stub
		
	}
	
}
