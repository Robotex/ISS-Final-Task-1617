%====================================================================================
% Context ctxHello  SYSTEM-configuration: file it.unibo.ctxHello.helloSystem.pl 
%====================================================================================
pubsubserveraddr("").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxhello, "localhost",  "TCP", "8010" ).  		 
%%% -------------------------------------------
qactor( qahello , ctxhello, "it.unibo.qahello.MsgHandle_Qahello"   ). %%store msgs 
qactor( qahello_ctrl , ctxhello, "it.unibo.qahello.Qahello"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

