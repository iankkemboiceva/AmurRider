/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package adapter;

public class TripDetails {

	private String cust;
	private String shippadr;

	private String amount;
	private String mobno;
	private String orderid;
	private String longt;
	private String latit;
	private String shworderid;
	private String txntimest;

	public TripDetails(String cust,String shippadr,String amount,String mobno,String orderid,String longt,String latit,String shworderid,String txntimest) {
		this.cust = cust;
this.shippadr = shippadr;
		this.amount = amount;
		this.mobno = mobno;
		this.orderid = orderid;
		this.latit = latit;
		this.longt = longt;
		this.shworderid = shworderid;
		this.txntimest = txntimest;
	}



	public String getCust() {
		return cust;
	}
	public void setCust(String bname) {
		this.cust = bname;
	}



    public String getShippAdr() {
        return shippadr;
    }
    public void setShippadr(String amon) {
        this.shippadr = amon;
    }

    public String getAmo() {
        return amount;
    }
    public void setAmount(String acct) {
        this.amount = acct;
    }

	public String getMobno() {
		return mobno;
	}
	public void setMobno(String acct) {
		this.mobno = acct;
	}

	public String getOrderId() {
		return orderid;
	}
	public void setOrderid(String acct) {
		this.orderid = acct;
	}

	public String getLongt() {
		return longt;
	}
	public void setLongt(String acct) {
		this.longt = acct;
	}

	public String getLatit() {
		return latit;
	}
	public void setLatit(String acct) {
		this.latit = acct;
	}

	public String getShworderid() {
		return shworderid;
	}

	public String getTxntimest() {
		return txntimest;
	}
}
