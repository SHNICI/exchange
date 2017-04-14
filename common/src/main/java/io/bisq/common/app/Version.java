/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.common.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Version {
    private static final Logger log = LoggerFactory.getLogger(Version.class);

    // The application versions
    // VERSION = 0.5.0.0 introduces proto buffer for the P2P network and local DB and is a not backward compatible update
    // Therefore all sub versions start again with 1
    public static final String VERSION = "0.5.0.0";

    // The version no. for the objects sent over the network. A change will break the serialization of old objects.
    // If objects are used for both network and database the network version is applied.
    // VERSION = 0.5.0.0 -> P2P_NETWORK_VERSION = 1
    @SuppressWarnings("ConstantConditions")
    public static final int P2P_NETWORK_VERSION = DevEnv.STRESS_TEST_MODE ? 100 : 1;

    // The version no. of the serialized data stored to disc. A change will break the serialization of old objects.
    // VERSION = 0.5.0.0 -> LOCAL_DB_VERSION = 1
    public static final int LOCAL_DB_VERSION = 1;

    // The version no. of the current protocol. The offer holds that version. 
    // A taker will check the version of the offers to see if his version is compatible.
    // VERSION = 0.5.0.0 -> TRADE_PROTOCOL_VERSION = 1
    public static final int TRADE_PROTOCOL_VERSION = 1;
    private static int p2pMessageVersion;

    public static final String BSQ_TX_VERSION = "1";

    public static int getP2PMessageVersion() {
        // TODO investigate why a changed NETWORK_PROTOCOL_VERSION for the serialized objects does not trigger 
        // reliable a disconnect., but java serialisation should be replaced anyway, so using one existing field
        // for the version is fine.
        return p2pMessageVersion;
    }

    // The version for the bitcoin network (Mainnet = 0, TestNet = 1, Regtest = 2)
    private static int BTC_NETWORK_ID;

    public static void setBtcNetworkId(int btcNetworkId) {
        BTC_NETWORK_ID = btcNetworkId;

        // BTC_NETWORK_ID  is 0, 1 or 2, we use for changes at NETWORK_PROTOCOL_VERSION a multiplication with 10 
        // to avoid conflicts:
        // E.g. btc BTC_NETWORK_ID=1, NETWORK_PROTOCOL_VERSION=1 -> getNetworkId()=2;
        // BTC_NETWORK_ID=0, NETWORK_PROTOCOL_VERSION=2 -> getNetworkId()=2; -> wrong
        p2pMessageVersion = BTC_NETWORK_ID + 10 * P2P_NETWORK_VERSION;
    }

    public static int getBtcNetworkId() {
        return BTC_NETWORK_ID;
    }

    public static void printVersion() {
        log.info("Version{" +
                "VERSION=" + VERSION +
                ", P2P_NETWORK_VERSION=" + P2P_NETWORK_VERSION +
                ", LOCAL_DB_VERSION=" + LOCAL_DB_VERSION +
                ", TRADE_PROTOCOL_VERSION=" + TRADE_PROTOCOL_VERSION +
                ", BTC_NETWORK_ID=" + BTC_NETWORK_ID +
                ", getP2PNetworkId()=" + getP2PMessageVersion() +
                '}');
    }

    public static final byte COMPENSATION_REQUEST_VERSION = (byte) 0x01;
    public static final byte VOTING_VERSION = (byte) 0x01;

}