/* Generated by AN DISI Unibo */ 
package it.unibo.sonarhandler;
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
public abstract class AbstractSonarhandler extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractSonarhandler(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/sonarhandler/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/sonarhandler/plans.txt";
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
	    	stateTab.put("handleEvents",handleEvents);
	    	stateTab.put("setStartDistance",setStartDistance);
	    	stateTab.put("processSonarData",processSonarData);
	    	stateTab.put("checkDistance",checkDistance);
	    	stateTab.put("handleTout",handleTout);
	    	stateTab.put("handleAlarm",handleAlarm);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "sonarhandler tout : stops");  
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
	    	//switchTo handleEvents
	        switchToPlanAsNextState(pr, myselfName, "sonarhandler_"+myselfName, 
	              "handleEvents",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun handleEvents = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_handleEvents",0);
	     pr.incNumIter(); 	
	    	String myselfName = "handleEvents";  
	    	//bbb
	     msgTransition( pr,myselfName,"sonarhandler_"+myselfName,false,
	          new StateFun[]{stateTab.get("processSonarData"), stateTab.get("setStartDistance"), stateTab.get("handleAlarm") }, 
	          new String[]{"true","E","sonar", "true","E","startDistance", "true","E","alarm" },
	          60000, "handleTout" );//msgTransition
	    }catch(Exception e_handleEvents){  
	    	 println( getName() + " plan=handleEvents WARNING:" + e_handleEvents.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleEvents
	    
	    StateFun setStartDistance = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("setStartDistance",-1);
	    	String myselfName = "setStartDistance";  
	    	temporaryStr = "\"setStartDistance\"";
	    	println( temporaryStr );  
	    	temporaryStr = "startingDistance(X)";
	    	removeRule( temporaryStr );  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("startDistance(X)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("startDistance") && 
	    		pengine.unify(curT, Term.createTerm("startDistance(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="startingDistance(X)";
	    			/* AddRule */
	    			parg = updateVars(Term.createTerm("startDistance(X)"),  Term.createTerm("startDistance(X)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) addRule(parg);	    		  					
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"sonarhandler_"+myselfName,false,true);
	    }catch(Exception e_setStartDistance){  
	    	 println( getName() + " plan=setStartDistance WARNING:" + e_setStartDistance.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//setStartDistance
	    
	    StateFun processSonarData = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("processSonarData",-1);
	    	String myselfName = "processSonarData";  
	    	parg = "updateSonarData";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	//switchTo checkDistance
	        switchToPlanAsNextState(pr, myselfName, "sonarhandler_"+myselfName, 
	              "checkDistance",false, true, null); 
	    }catch(Exception e_processSonarData){  
	    	 println( getName() + " plan=processSonarData WARNING:" + e_processSonarData.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//processSonarData
	    
	    StateFun checkDistance = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkDistance",-1);
	    	String myselfName = "checkDistance";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " not !?startingDistance(X)" )) != null )
	    	{
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar1,TARGET,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonar") && 
	    		pengine.unify(curT, Term.createTerm("sonar(SONARNAME,TARGETNAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="startingDistance(DISTANCE)";
	    			parg = updateVars( Term.createTerm("sonar(SONARNAME,TARGETNAME,DISTANCE)"),  Term.createTerm("sonar(sonar1,TARGET,DISTANCE)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) addRule(parg);	    		  					
	    	}
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar1,TARGET,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonar") && 
	    		pengine.unify(curT, Term.createTerm("sonar(SONARNAME,TARGETNAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?isatdistance(sonar1)" )) != null ){
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " ??adjustingDistance" )) != null ){
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"stop\",40,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			}
	    			};//actionseq
	    			}
	    			else{ {//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " not !?adjustingDistance" )) != null )
	    			{
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?isfarther(sonar1)" )) != null ){
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"forward\",20,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			}
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?isnearer(sonar1)" )) != null ){
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"backward\",20,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			}
	    			temporaryStr = "adjustingDistance";
	    			addRule( temporaryStr );  
	    			};//actionseq
	    			}
	    			};//actionseq
	    			}};//actionseq
	    	}
	    	else{ {//actionseq
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar2,TARGET,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonar") && 
	    		pengine.unify(curT, Term.createTerm("sonar(SONARNAME,TARGETNAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?isatdistance(sonar2)" )) != null ){
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " ??adjustingDistance" )) != null ){
	    			{//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"stop\",40,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			};//actionseq
	    			}
	    			else{ {//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "alarm(X)","alarm(sonar2)", guardVars ).toString();
	    			emit( "alarm", temporaryStr );
	    			};//actionseq
	    			}};//actionseq
	    			}
	    			else{ {//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " not !?adjustingDistance" )) != null )
	    			{
	    			{//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "alarm(X)","alarm(sonar2)", guardVars ).toString();
	    			emit( "alarm", temporaryStr );
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"left\",40,1000))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?isfarther(sonar2)" )) != null ){
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"forward\",20,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			}
	    			else{ temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"backward\",20,0))", guardVars ).toString();
	    			sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    			}temporaryStr = "adjustingDistance";
	    			addRule( temporaryStr );  
	    			};//actionseq
	    			}
	    			};//actionseq
	    			}};//actionseq
	    	}
	    	else{ {//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??adjustingDistance" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"stop\",100,0))", guardVars ).toString();
	    	sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"right\",40,1000))", guardVars ).toString();
	    	sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(CMD)","cmd(move(\"backward\",1,0))", guardVars ).toString();
	    	sendMsg("moveRover","rover", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	};//actionseq
	    	}};//actionseq
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"sonarhandler_"+myselfName,false,true);
	    }catch(Exception e_checkDistance){  
	    	 println( getName() + " plan=checkDistance WARNING:" + e_checkDistance.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkDistance
	    
	    StateFun handleTout = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    	String myselfName = "handleTout";  
	    	repeatPlanNoTransition(pr,myselfName,"sonarhandler_"+myselfName,false,true);
	    }catch(Exception e_handleTout){  
	    	 println( getName() + " plan=handleTout WARNING:" + e_handleTout.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleTout
	    
	    StateFun handleAlarm = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleAlarm",-1);
	    	String myselfName = "handleAlarm";  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("alarm(fire)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("alarm") && 
	    		pengine.unify(curT, Term.createTerm("alarm(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " ??adjustingDistance" )) != null ){
	    			temporaryStr = "\"[sonarhandler] detected fire alarm while I had control, cleared out.\"";
	    			temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    			println( temporaryStr );  
	    			}
	    			};//actionseq
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"sonarhandler_"+myselfName,false,true);
	    }catch(Exception e_handleAlarm){  
	    	 println( getName() + " plan=handleAlarm WARNING:" + e_handleAlarm.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleAlarm
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
