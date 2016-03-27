/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:29 PM
 */
@RestController
@RequestMapping("user")
@SuppressWarnings("unused")
public class UserController {
   protected final Logger log = LoggerFactory.getLogger(getClass());
}
