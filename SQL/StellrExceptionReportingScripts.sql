-- Create table script

CREATE TABLE `txnexception` (
  `txnexceptionid` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchantid` varchar(255) DEFAULT NULL,
  `rrn` varchar(255) DEFAULT NULL,
  `transactionuniqueidentifier` varchar(255) DEFAULT NULL,
  `donewhen` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(255) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `severity` int(1) DEFAULT NULL,
  PRIMARY KEY (`txnexceptionid`),
  KEY `index_tui` (`transactionuniqueidentifier`),
  KEY `index_merchant` (`merchantid`),
  KEY `index_tui_merchant` (`transactionuniqueidentifier`,`merchantid`),
  KEY `index_idid` (`txnexceptionid`),
  KEY `index_rrn` (`rrn`)
) ENGINE=InnoDB AUTO_INCREMENT=1205 DEFAULT CHARSET=latin1;



-- Alter table script 

ALTER TABLE txnexception
ADD category varchar(100) DEFAULT NULL;

ALTER TABLE txnexception
ADD severity  int(1) DEFAULT NULL;

-- Initialize historic data
Update txnexception set category = 'System' 
where txnexceptionid > 0
and category is NULL; 

Update txnexception set severity = 3 	   
where txnexceptionid > 0
and   severity IS NULL;

-- Exception Reporting Stored Procedures

DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionsReportDates`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionsReportDates`(in fromDate timestamp, 
																		  in toDate timestamp,
																		  OUT ret VARCHAR(255) )
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionsReportDates Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
            
    set ret = '';
    
    select  COALESCE(description,"") as description ,  
		   donewhen as donewhen, 
           COALESCE(severity,"") as severity , 
           COALESCE(merchantid,"") as merchantid ,
           COALESCE(txnexceptionid, 0) as txnexceptionid , 
           COALESCE(rrn, 0) as rrn , 
           COALESCE(transactionuniqueidentifier, 0) as transactionuniqueidentifier , 
           COALESCE(category,"") as category 
	from txnexception 
    where donewhen >= fromDate 
    and   donewhen <= toDate
	order by donewhen desc
    limit 10;
        
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionsReportDatesMain`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionsReportDatesMain`(in fromDate timestamp, 
																		      in toDate timestamp,
																			  OUT ret VARCHAR(255))
BEGIN
	 DECLARE code CHAR(5) DEFAULT '00000';
	 DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionsReportDatesMain Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
    
	 set ret = '';
      
     select COALESCE(b.description,"") as description ,  
		   b.donewhen as donewhen, 
           COALESCE(b.severity,"") as severity , 
           COALESCE(b.merchantid,"") as merchantid ,
           COALESCE(b.txnexceptionid, 0) as txnexceptionid , 
           COALESCE(b.rrn, 0) as rrn , 
           COALESCE(b.transactionuniqueidentifier, 0) as transactionuniqueidentifier , 
           COALESCE(b.category,"") as category ,
           COALESCE(transactionentry.transactionid,"") as transactionid ,
           COALESCE(transactionentry.primaryaccountnumber,"") as primaryaccountnumber ,
           COALESCE(transactionentry.processingcode,"") as processingcode ,
           COALESCE(transactionentry.transactionamount,"") as transactionamount ,
           COALESCE(transactionentry.responsecode,"") as responsecode ,
           COALESCE(transactionentry.productid,"") as productid ,
           COALESCE(transactionentry.productstatus,"") as productstatus ,
           COALESCE(transactionentry.secondaryrespcode,"") as secondaryrespcode ,
           COALESCE(transactionentry.transactionstate,"") as transactionstate 
	from txnexception as b 
    left join transactionentry on transactionentry.transactionuniqueidentifier = b.transactionuniqueidentifier
	and   transactionentry.merchantid = b.merchantid
    and   transactionentry.transactionid in 
				( select max(a.transactionid) 
				  from transactionentry as a 
                  where a.transactionuniqueidentifier =  b.transactionuniqueidentifier
                  and   a.merchantid =  b.merchantid
                   ) 
	where b.donewhen >= fromDate 
    and   b.donewhen <= toDate
	order by b.donewhen desc;
    
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionTotal`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionTotal`(in fromDate timestamp, 
															       in toDate timestamp,
																   OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionTotal Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
     
    select count(1)
	from txnexception
    where donewhen >= fromDate 
    and   donewhen <= toDate;	  
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionHighestSeverity`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionHighestSeverity`(in fromDate timestamp, 
																		     in toDate timestamp,
																			 OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionHighestSeverity Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
 
    select concat ('Severity Level ', COALESCE(severity,"") ) as severityLevel 
	from txnexception
    where donewhen >= fromDate 
    and   donewhen <= toDate
    order by severity
    limit 1;	  
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionCategories`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionCategories`(OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionCategories Exception ( ) code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
     
    select count(1), category 
	from txnexception 
	group by category;
    
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionseverity`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionseverity`(OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionseverity Exception (  ) code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
     
    select count(1), 
		concat ('Severity Level ', COALESCE(severity,"") ) as severityLevel 
	from txnexception 
	group by severity;
    
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionCategoriesDate`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionCategoriesDate`(in fromDate timestamp, 
																		    in toDate timestamp,
																			OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionCategoriesDate Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
     
    select count(1), category 
	from txnexception 
    where donewhen >= fromDate 
    and   donewhen <= toDate
	group by category;
    
    
END$$
DELIMITER ;



DELIMITER $$
DROP PROCEDURE IF EXISTS `getTXNExceptionseverityDates`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getTXNExceptionseverityDates`(in fromDate timestamp, 
																		   in toDate timestamp,
																		   OUT ret VARCHAR(255))
BEGIN
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    
	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('getTXNExceptionseverityDates Exception ( ', fromDate, ' , ' , toDate, ') code: ', code, ' message: ', msg) INTO ret;
            END;
    
	set ret = '';
     
    select count(1), 
			concat ('Severity Level ', COALESCE(severity,"") ) as severityLevel 
	from txnexception 
    where donewhen >= fromDate 
    and   donewhen <= toDate
	group by severity;
    
    
END$$
DELIMITER ;




-- Script used for testing generating fake exceptions 

DELIMITER $$
DROP PROCEDURE IF EXISTS `loopInsertExceptions`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `loopInsertExceptions`(IN limitCnt INTEGER)
BEGIN
 
  DECLARE  v_merchantid TEXT;
  DECLARE  v_rrn TEXT;
  DECLARE  v_transactionuniqueidentifier TEXT;
  DECLARE  v_description TEXT;
  DECLARE  v_category TEXT; 
  DECLARE  v_severity INTEGER;
 
  DECLARE v_finished INTEGER DEFAULT 0;
  
 -- declare cursor for employee email
 DEClARE x_cursor CURSOR FOR 
 select merchantid, rrn, transactionuniqueidentifier,  description, category, severity from txnexception  order by  RAND() limit limitCnt;
 
 -- declare NOT FOUND handler
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET v_finished = 1;
        
 OPEN x_cursor;
 
 get_x: LOOP
 
 FETCH x_cursor INTO v_merchantid, v_rrn, v_transactionuniqueidentifier, v_description, v_category, v_severity;
 
 IF v_finished = 1 THEN 
 LEAVE get_x;
 END IF;
 
 -- Insert
 insert into txnexception ( merchantid, rrn, transactionuniqueidentifier, description, category, severity ) values 
						  ( v_merchantid, v_rrn, v_transactionuniqueidentifier, v_description, v_category, v_severity ) ;
 
 DO SLEEP(10);
 
 END LOOP get_x;
 
 CLOSE x_cursor;
 
END$$
DELIMITER ;








