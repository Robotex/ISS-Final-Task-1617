%====================================================================================
% Context ctxRover  SYSTEM-configuration: file it.unibo.ctxRover.rover.pl 
%====================================================================================
context(ctxrover, "localhost",  "TCP", "8090" ).  		 
%%% -------------------------------------------
qactor( sonarhandler , ctxrover, "it.unibo.sonarhandler.MsgHandle_Sonarhandler"   ). %%store msgs 
qactor( sonarhandler_ctrl , ctxrover, "it.unibo.sonarhandler.Sonarhandler"   ). %%control-driven 
qactor( rover , ctxrover, "it.unibo.rover.MsgHandle_Rover"   ). %%store msgs 
qactor( rover_ctrl , ctxrover, "it.unibo.rover.Rover"   ). %%control-driven 
qactor( rovermind , ctxrover, "it.unibo.rovermind.MsgHandle_Rovermind"   ). %%store msgs 
qactor( rovermind_ctrl , ctxrover, "it.unibo.rovermind.Rovermind"   ). %%control-driven 
qactor( console , ctxrover, "it.unibo.console.MsgHandle_Console"   ). %%store msgs 
qactor( console_ctrl , ctxrover, "it.unibo.console.Console"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(evh,ctxrover,"it.unibo.ctxRover.Evh","sonar").  
%%% -------------------------------------------

