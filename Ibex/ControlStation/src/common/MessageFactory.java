package common;

import messages.*;

public class MessageFactory 
{

	 //use getShape method to get object of type shape 
   public static Message makeMessage(MessageType type){

//	   System.out.println(type.toString());
	   if(type == null){ return null; }
	   else if(type == MessageType.MSG_STOP) { return new MsgStop(); }
	   else if(type == MessageType.MSG_DRIVE_TIME) { return new MsgDriveTime(); }
	   else if(type == MessageType.MSG_DRIVE_DISTANCE) { return new MsgDriveDistance(); }
	   else if(type == MessageType.MSG_ROTATE_TIME) { return new MsgRotateTime(); }
	   else if(type == MessageType.MSG_SCOOP_TIME) { return new MsgScoopTime(); }
	   //else if(type == MessageType.MSG_SCOOP_DISTANCE) { return new MsgScoopDistance(); }
	   else if(type == MessageType.MSG_ROTATE_TO_CENTER) { return new MsgBucketTime(); }
	   //else if(type == MessageType.MSG_BUCKET_DISTANCE) { return new MsgBucketDistance(); }
	   else if(type == MessageType.MSG_GET_TARGET) { return new MsgGetTarget(); }
	   //else if(type == MessageType.MSG_STOP_TIME) { return new MsgStopTime(); }
	   else if(type == MessageType.MSG_MOTOR_VALUES) { return new MsgMotorValues(); }
	   else if(type == MessageType.MSG_ROTATE_TO_PERPENDICULAR) { return new MsgRatchetPosition(); }
	   else if (type == MessageType.MSG_DIG){ return new MsgDigTime();}
	   else if(type == MessageType.MSG_PING) {return new MsgPingTest();}
	   else if(type == MessageType.MSG_RETRACT_DIGGER) {return new MsgRetractDigger();}
	   else if(type == MessageType.MSG_GO_TO_DUMP) {return new MsgGoToDump();}
	   else if(type == MessageType.MSG_DUMP){return new MsgDump();}
	   else if(type == MessageType.MSG_DEPTH_TIME){return new MsgDepthTime();}

      
      return null;
   }
	
}
