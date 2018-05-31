%====================================================================================
% Context ctxRover4  SYSTEM-configuration: file it.unibo.ctxRover4.testRover4.pl 
%====================================================================================
context(ctxrover4, "localhost",  "TCP", "8090" ).  		 
%%% -------------------------------------------
qactor( sonarhandler , ctxrover4, "it.unibo.sonarhandler.MsgHandle_Sonarhandler"   ). %%store msgs 
qactor( sonarhandler_ctrl , ctxrover4, "it.unibo.sonarhandler.Sonarhandler"   ). %%control-driven 
qactor( rover , ctxrover4, "it.unibo.rover.MsgHandle_Rover"   ). %%store msgs 
qactor( rover_ctrl , ctxrover4, "it.unibo.rover.Rover"   ). %%control-driven 
qactor( rovermind , ctxrover4, "it.unibo.rovermind.MsgHandle_Rovermind"   ). %%store msgs 
qactor( rovermind_ctrl , ctxrover4, "it.unibo.rovermind.Rovermind"   ). %%control-driven 
qactor( console , ctxrover4, "it.unibo.console.MsgHandle_Console"   ). %%store msgs 
qactor( console_ctrl , ctxrover4, "it.unibo.console.Console"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(evh,ctxrover4,"it.unibo.ctxRover4.Evh","sonar").  
%%% -------------------------------------------

