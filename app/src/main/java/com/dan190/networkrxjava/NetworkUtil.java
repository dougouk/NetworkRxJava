package com.dan190.networkrxjava;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dan on 28/09/2017.
 */

public class NetworkUtil {
    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    /**
     * Retrieve a port that is open
     * @return int that represents the open port. [16403, 65535]
     * @throws Exception in case there is no open port
     */
    public static int getOpenPort() throws Exception {
        int start = 16403;
        int max = 65535;
        ServerSocket socket = null;
        boolean available = false;
        for(int i = start; i < max; i++){
            try{
                socket = new ServerSocket(i);
                available = true;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket != null){
                    try{
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(available){
                return i;
            }
        }
        throw new Exception("No available ports");
    }

}
