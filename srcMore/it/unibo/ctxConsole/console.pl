%====================================================================================
% Context ctxConsole  SYSTEM-configuration: file it.unibo.ctxConsole.console.pl 
%====================================================================================
pubsubserveraddr("").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxconsole, "localhost",  "TCP", "8010" ).  		 
%%% -------------------------------------------
qactor( rover , ctxconsole, "it.unibo.rover.MsgHandle_Rover"   ). %%store msgs 
qactor( rover_ctrl , ctxconsole, "it.unibo.rover.Rover"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

