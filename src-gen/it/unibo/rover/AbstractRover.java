/* Generated by AN DISI Unibo */ 
package it.unibo.rover;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractRover extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRover(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/rover/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/rover/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitUserCmd",waitUserCmd);
	    	stateTab.put("execMove",execMove);
	    	stateTab.put("connectToUnity",connectToUnity);
	    	stateTab.put("doconnectToUnity",doconnectToUnity);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "rover tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"rover started\"";
	    	println( temporaryStr );  
	    	//switchTo waitUserCmd
	        switchToPlanAsNextState(pr, myselfName, "rover_"+myselfName, 
	              "waitUserCmd",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitUserCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitUserCmd",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitUserCmd";  
	    	//bbb
	     msgTransition( pr,myselfName,"rover_"+myselfName,false,
	          new StateFun[]{stateTab.get("execMove") },//new StateFun[]
	          new String[]{"true","E","usercmd" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitUserCmd){  
	    	 println( getName() + " plan=waitUserCmd WARNING:" + e_waitUserCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitUserCmd
	    
	    StateFun execMove = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("execMove",-1);
	    	String myselfName = "execMove";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(unityAddr(X)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			/* SwitchTransition */
	    			String parg = "connectToUnity";
	    			parg =  updateVars( Term.createTerm("usercmd(CMD)"), 
	    				                Term.createTerm("usercmd(robotgui(unityAddr(X)))"), 
	    				                Term.createTerm(currentEvent.getMsg()), parg);
	    			if(parg != null){ 
	    				switchToPlanAsNextState(pr, myselfName, "console_"+myselfName, 
	    			    	 		    		parg,false, true, null); 
	    			    return;	
	    			    //the control is given to the caller state
	    			}
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"rover_"+myselfName,false,true);
	    }catch(Exception e_execMove){  
	    	 println( getName() + " plan=execMove WARNING:" + e_execMove.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//execMove
	    
	    StateFun connectToUnity = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("connectToUnity",-1);
	    	String myselfName = "connectToUnity";  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(unityAddr(ADDR)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg = "ADDR";
	    			/* Print */
	    			parg =  updateVars( Term.createTerm("usercmd(CMD)"), 
	    			                    Term.createTerm("usercmd(robotgui(unityAddr(ADDR)))"), 
	    				    		  	Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) println( parg );
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?unityOn" )) != null ){
	    	temporaryStr = "\"UNITY already connected\"";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	//switchTo doconnectToUnity
	        switchToPlanAsNextState(pr, myselfName, "rover_"+myselfName, 
	              "doconnectToUnity",false, true, " not !?unityOn" 
	              ); 
	    }catch(Exception e_connectToUnity){  
	    	 println( getName() + " plan=connectToUnity WARNING:" + e_connectToUnity.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//connectToUnity
	    
	    StateFun doconnectToUnity = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doconnectToUnity",-1);
	    	String myselfName = "doconnectToUnity";  
	    	initUnityConnection( "192.168.203.130" );  
	    	createSimulatedActor("rover", "Prefabs/CustomActor"); 
	    	execUnity("rover","backward",800, 70,0); //rover: default namefor virtual robot		
	    	execUnity("rover","right",1000, 70,0); //rover: default namefor virtual robot		
	    	temporaryStr = "unityOn";
	    	addRule( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"rover_"+myselfName,false,true);
	    }catch(Exception e_doconnectToUnity){  
	    	 println( getName() + " plan=doconnectToUnity WARNING:" + e_doconnectToUnity.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doconnectToUnity
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}