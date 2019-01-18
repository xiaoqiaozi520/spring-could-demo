package cn.ymdd.framework.toolkit.util;

import cn.ymdd.framework.toolkit.util.StringUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public abstract class NetworkUtil
{
    public static long iP2Long(String ip)
    {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0L;
        }
        int p1 = Integer.valueOf(p[0]).intValue() << 24 & 0xFF000000;
        int p2 = Integer.valueOf(p[1]).intValue() << 16 & 0xFF0000;
        int p3 = Integer.valueOf(p[2]).intValue() << 8 & 0xFF00;
        int p4 = Integer.valueOf(p[3]).intValue() << 0 & 0xFF;
        return (p1 | p2 | p3 | p4) & 0xFFFFFFFF;
    }

    public static String long2IP(long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip >> 24 & 0xFF).append('.').append(ip >> 16 & 0xFF).append('.').append(ip >> 8 & 0xFF).append('.').append(ip >> 0 & 0xFF);
        return sb.toString();
    }

    public static String getInnerIP() {
        String ip = getServerAddress(IpMode.Inner).getHostAddress();
        if (noMappingIP(ip)) {
            throw new NullPointerException("IP");
        }
        return ip;
    }

    public static String getOuterIP()
    {
        String ip = getServerAddress(IpMode.Outer).getHostAddress();
        if (noMappingIP(ip)) {
            throw new NullPointerException("IP");
        }
        return ip;
    }

    public static InetAddress getServerAddress(IpMode mode)
    {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
                if (isFictitious(networkInterface.getName())) {
                    continue;
                }
                Enumeration inAddresses = networkInterface.getInetAddresses();
                while (inAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)inAddresses.nextElement();
                    String ip = inetAddress.getHostAddress();
                    if ((!StringUtil.isBlank(ip)) && ((inetAddress instanceof Inet4Address))) {
                        switch (mode.ordinal()) {
                            case 1:
                                if ((!inetAddress.isSiteLocalAddress()) || (inetAddress.isAnyLocalAddress())) break;
                                return inetAddress;
                            case 2:
                                if ((inetAddress.isLoopbackAddress()) || (inetAddress.isSiteLocalAddress())) break;
                                return inetAddress;
                            case 3:
                                if (!inetAddress.isLoopbackAddress()) break;
                                return inetAddress;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Server address error: {}", e);
        }
        throw new RuntimeException("Can not find server real address");
    }

    private static boolean noMappingIP(String ip) {
        return (StringUtil.isBlank(ip)) || ("unknown".equalsIgnoreCase(ip));
    }

    private static boolean isFictitious(String name) {
        return "docker0".equals(name);
    }

    public static enum IpMode {
        Inner, Outer, LoopBack;
    }
}