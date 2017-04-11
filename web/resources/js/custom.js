/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */
$(document).ready(function() {
	      $(document).foundation();
	      
	      //add up to date copyright text to footer
	      var dates = new Date();
	      var thisYear = dates.getFullYear();
	      $('#footer').html('<h6 class="text-right">HALO Exception Reporting | &copy; Stellr ' + thisYear + '</h6>');
	   })