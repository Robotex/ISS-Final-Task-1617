package it.unibo.custom;



import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;
import java.util.Stack;

import alice.tuprolog.Int;
import alice.tuprolog.Prolog;

import java.util.Queue;
import java.util.Iterator;
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
		public String formatAsMessage(Prolog prologEngine){
			return QActorUtils.unifyMsgContent(prologEngine, this.move +"(TIME)", this.move + "(" + this.time + ")", null ).toString();			
		}
	}
	public static void register(QActor myself, String move, String time) {
		stack.add(new Action(move, time));
	}
	
	public static void startReverse(QActor myself) throws Exception{
		System.out.println("starting reverse" );
		Iterator<Action> iterator = stack.iterator();
		Runnable reverse = () -> { 
			while(iterator.hasNext()){
				Action curraction = iterator.next();
				System.out.println("sending:" + curraction.toString());
				try {					
					myself.sendMsg(curraction.move, "rover", "dispatch", curraction.formatAsMessage(myself.getPrologEngine()));
					Thread.sleep(Integer.parseInt(curraction.time) + 2000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		};
		reverse.run();		
	}
}
