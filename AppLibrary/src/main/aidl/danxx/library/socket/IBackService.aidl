package danxx.library.socket;
//aidl接口，用于线程通讯
interface IBackService{
	boolean sendMessage(String message);
}
