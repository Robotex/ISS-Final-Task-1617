package it.unibo.custom;


import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;
import java.util.Stack;
import java.util.stream.Collectors;

import alice.tuprolog.Int;
import alice.tuprolog.Prolog;
import alice.tuprolog.Term;
import cli.System.DateTime;
import cli.System.TimeSpan;

import java.util.Queue;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class path {
	
	static Deque<Action> stack = new ArrayDeque<Action>();
	
	
	public static class Action {
		public String move;
		public String time;
		private long timeStamp;
		private long actualDuration;
		
		public Action(Action other) {
			this.move = other.move;
			this.time = other.time;
			this.timeStamp = other.timeStamp;
			this.actualDuration = other.actualDuration;
		}
		
		public Action(String move, String time) {
			this.move = move;
			this.time = time;
			this.timeStamp = System.currentTimeMillis();			
		}
		
		public Action(String move, String time, String timeout) {
			this.move = move;
			this.time = time;
			this.timeStamp = System.currentTimeMillis();
			if (time.isEmpty() || time == "0")
				this.actualDuration = Long.parseLong(timeout);			
			
		}
		public String toString(){
			return getCmd();
		}
		public Action reverse() {
			Action reversed = new Action(this);
			Action nextMove =stack.peek();
			
			/*
			
			if (reversed.move.equals("left"))
				reversed.move = "right";
			else if (reversed.move.equals("right"))
				reversed.move = "left";*/
			
			if (nextMove != null && nextMove.move!="backward")
			{
				if (reversed.move.equals("left")) reversed.move="right";
				else if (reversed.move.equals("right")) reversed.move="left";
			}
			if (reversed.move.equals("backward")) reversed.move="forward";
			return reversed;
		}
		
		public String formatAsMessage(Prolog prologEngine){
			//return QActorUtils.unifyMsgContent(prologEngine, this.move +"(TIME)", this.move + "(" + this.time + ")", null ).toString();	
			String parg=getCmd();
			//parg = updateVars(Term.createTerm("usercmd(executeInput(X))"),  Term.createTerm("usercmd(executeInput(move(X,Y,Z)))"), 
  			//		Term.createTerm(currentEvent.getMsg()), parg);
			return parg;
		}
		
		public String getCmd() {
			String parg;
			if (this.actualDuration > 0)
				parg="cmd(move(\"" + this.move + "\",20," + this.actualDuration + "))";
			else
				parg="cmd(move(\"" + this.move + "\",20," + this.time + "))";
			return parg;
		}
	}
	
	
	public static void register(QActor myself, String move, String time) {
		Action action = new Action(move, time);
		
		if (!stack.isEmpty()) {
			
			Action lastAction = stack.peek();
			lastAction.actualDuration = System.currentTimeMillis() - lastAction.timeStamp; 
		}
		else
		{
			stack.push(new Action("stop","20","750"));
		}
		
		myself.println("[console] register: " + action.getCmd() + " reversed: " + action.reverse().getCmd());

		stack.push(action);		
	}
	public static void register(QActor myself, String move, String time, String timeout) {
		Action action = new Action(move, time, timeout);		
		if(action.move == "stop" && stack.size() > 0){
			Action lastAction = stack.peek();
			lastAction.actualDuration = System.currentTimeMillis() - lastAction.timeStamp; 
		}
		stack.push(action);
	}
	
	public static void startReverse(QActor myself){

			try {
				stack= new ArrayDeque<Action>(stack.stream().filter(f->!f.move.equals("stop")).collect(Collectors.toList()));
				
				myself.println("[console] startReverse rotating rover");
				myself.sendMsg("moveRover", "rover", QActorContext.dispatch, "cmd(move(right,20,750))");
				Thread.sleep(1500);
				myself.sendMsg("moveRover", "rover", QActorContext.dispatch, "cmd(move(right,20,750))");
				Thread.sleep(1500);
				
				myself.println("[console] startReverse executing path");
			
				//for (Action curraction : stack)
				//Action curraction;
				while(!stack.isEmpty())
				{
					
					Action reversedaction = stack.pop().reverse();

						myself.sendMsg("moveRover", "rover", QActorContext.dispatch, reversedaction.formatAsMessage(myself.getPrologEngine()));
						Thread.sleep(reversedaction.actualDuration);
				}
				myself.sendMsg("moveRover","rover",QActorContext.dispatch," cmd(move(stop,20,750))");
			
			} catch (Exception e) {	
				myself.println("[console] startReverse Exception! " + e.getLocalizedMessage());
			}
	}
}