package it.unibo.custom;


import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;
import java.util.Stack;

import alice.tuprolog.Int;
import alice.tuprolog.Prolog;
import alice.tuprolog.Term;
import cli.System.TimeSpan;

import java.util.Queue;
import java.util.Iterator;
import java.util.LinkedList;

public class path {
	
	static Stack<Action> stack = new Stack<Action>();
	
	public static class Action{
		public String move;
		public String time;
		private long timeStamp;
		private long actualDuration;
		public Action(String move, String time){
			this.move = move;
			this.time = time;
			this.timeStamp = System.currentTimeMillis();			
		}
		
		public Action(String move, String time, String timeout){
			this.move = move;
			this.time = time;
			this.timeStamp = System.currentTimeMillis();
			if(time == "0" || time.isEmpty())
				this.actualDuration = Long.parseLong(timeout);			
			
		}
		public String toString(){
			return move + "("+time+")";
		}
		public String formatAsMessage(Prolog prologEngine){
			//return QActorUtils.unifyMsgContent(prologEngine, this.move +"(TIME)", this.move + "(" + this.time + ")", null ).toString();	
			String parg="cmd(move(\"" + this.move + "\",20," + this.actualDuration + "))";
			//parg = updateVars(Term.createTerm("usercmd(executeInput(X))"),  Term.createTerm("usercmd(executeInput(move(X,Y,Z)))"), 
  			//		Term.createTerm(currentEvent.getMsg()), parg);
			return parg;
		}
	}
	
	
	public static void register(QActor myself, String move, String time) {
		Action action = new Action(move, time);
		
		if(action.move == "stop" && stack.size() > 0){
			Action lastAction = stack.get(stack.size() - 1);
			lastAction.actualDuration = System.currentTimeMillis() - lastAction.timeStamp; 
		}
		stack.add(action);		
	}
	public static void register(QActor myself, String move, String time, String timeout) {
		Action action = new Action(move, time, timeout);		
		if(action.move == "stop" && stack.size() > 0){
			Action lastAction = stack.get(stack.size() - 1);
			lastAction.actualDuration = System.currentTimeMillis() - lastAction.timeStamp; 
		}
		stack.add(action);
	}
	
	public static void startReverse(QActor myself) throws Exception{
		System.out.println("starting reverse" );
		Iterator<Action> iterator = stack.iterator();
		Runnable reverse = () -> { 
			while(iterator.hasNext()){
				Action curraction = iterator.next();
				System.out.println("sending:" + curraction.toString());
				try {					
					myself.sendMsg("moveRover", "rover", QActorContext.dispatch, curraction.formatAsMessage(myself.getPrologEngine()));
					Thread.sleep(Integer.parseInt(curraction.time) + 2000);
				} catch (Exception e) {	}
			}	
		};
		reverse.run();		
	}
}