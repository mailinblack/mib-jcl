package com.mib.net;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

import static org.apache.commons.lang3.StringUtils.*;

public enum MibNetUtils {
    ;

    public static boolean isValidIp4Cidr(final String cidr) {
        if (isBlank(cidr)) {
            return false;
        }
        final String[] cidrSplited = cidr.split("\\/");
        if (cidrSplited.length != 2) {
            return false;
        }

        final String cidrAddress = cidrSplited[0];
        final String cidrSize = cidrSplited[1];
        int cidrSizeNum;

        try {
            cidrSizeNum = Integer.parseInt(cidrSize);
        } catch (final Exception e) {
            return false;
        }

        return cidrSizeNum >= 0 && cidrSizeNum <= 32 && isValidIp4(cidrAddress);
    }

    public static boolean isValidIp4(final String ip) {
        if (isBlank(ip)) {
            return false;
        }

        final InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(ip);
    }

    public static boolean isIp4InCidr(final String ip, final String cidr) {
        if (!isValidIp4(ip) || !isValidIp4Cidr(cidr)) {
            return false;
        }
        if (cidr.equals("0.0.0.0/0")) {
            return true;
        }
        final SubnetUtils subnetUtils = new SubnetUtils(cidr);
        subnetUtils.setInclusiveHostCount(true);

        return subnetUtils.getInfo().isInRange(ip);
    }
}
