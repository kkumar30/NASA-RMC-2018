package common;

import messages.*;

public class MessageFactory 
{

	 //use getShape method to get object of type shape 
   public static Message makeMessage(MessageType type){
	   
	   if(type == null){ return null; }
	   else if(type == MessageType.MSG_STOP) { return new MsgStop(); }
	   else if(type == MessageType.MSG_DRIVE_TIME) { return new MsgDriveTime(); }
	   else if(type == MessageType.MSG_DRIVE_DISTANCE) { return new MsgDriveDistance(); }
	   else if(type == MessageType.MSG_ROTATE_TIME) { return new MsgRotateTime(); }
	   else if(type == MessageType.MSG_SCOOP_TIME) { return new MsgScoopTime(); }
	   //else if(type == MessageType.MSG_SCOOP_DISTANCE) { return new MsgScoopDistance(); }
	   else if(type == MessageType.MSG_BUCKET_TIME) { return new MsgBucketTime(); }
	   //else if(type == MessageType.MSG_BUCKET_DISTANCE) { return new MsgBucketDistance(); }
	   else if(type == MessageType.MSG_BUCKET_POSITION) { return new MsgBucketPosition(); }
	   //else if(type == MessageType.MSG_STOP_TIME) { return new MsgStopTime(); }
	   else if(type == MessageType.MSG_MOTOR_VALUES) { return new MsgMotorValues(); }
	   else if(type == MessageType.MSG_RATCHET_POSITION) { return new MsgRatchetPosition(); }
      
      return null;
   }
	
}
