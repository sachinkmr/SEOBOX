/*
* Copyright (c) 2015, Anshoo Arora (Relevant Codes).  All rights reserved.
* 
* Copyrights licensed under the New BSD License.
* 
* See the accompanying LICENSE file for terms.
*/

package com.relevantcodes.extentreports.view;

import com.relevantcodes.extentreports.LogStatus;

public class Icon {
    public String getIcon(LogStatus status) {
	if (status == null) {
	    status = LogStatus.UNKNOWN;
	}

	String s = status.toString().toLowerCase();
	if (s.equals("fail")) {
	    return "mdi-navigation-cancel";
	}
	if (s.equals("fatal")) {
	    return "mdi-navigation-cancel";
	}
	if (s.equals("error")) {
	    return "mdi-alert-error";
	}
	if (s.equals("warning")) {
	    return "mdi-alert-warning";
	}
	if (s.equals("pass")) {
	    return "mdi-action-check-circle";
	}
	if (s.equals("info")) {
	    return "mdi-action-info-outline";
	}
	if (s.equals("skip")) {
	    return "mdi-content-redo";
	}
	return "mdi-action-help";
    }

    public Icon() {
    }
}
