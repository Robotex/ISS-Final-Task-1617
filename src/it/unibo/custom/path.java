package it.unibo.custom;



import it.unibo.qactors.akka.QActor;
import java.util.Stack;

import alice.tuprolog.Int;

import java.util.Queue;
import java.util.LinkedList;

public class path {
	
	static Stack<Action> stack = new Stack<Action>();
	
	public static class Action{
		public String move;
		public String time;
		public Action(String move, String time){
			this.move = move.split(":")[1];
			this.time = time;
		}
		public String toString(){
			return move + "("+time+")";
		}
	}
	public static void register(QActor myself, String move, String time) {
		Action action = new Action(move, time);
		stack.add(action);
		System.out.println(action.toString());
		
		if(stack.size() == 3){
			stack.stream().forEach(curraction -> {
				try {
					myself.sendMsg(curraction.move, "rover", "dispatch", "0");
					Thread.sleep(Integer.parseInt(action.time));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
}
