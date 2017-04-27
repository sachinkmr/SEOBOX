<#assign dateFormat = "dd-MMMM-YYYY">
<#assign timeFormat = "h:mm:ss a">
<#assign dateTimeFormat = "dd-MMMM-YYYY" + " " + "h:mm:ss a">

<!DOCTYPE html>
<html>
	<head>
		<!--
			ExtentReports Library 2.41.1 | http://relevantcodes.com/extentreports-for-selenium/ | https://github.com/anshooarora/
			Copyright (c) 2015, Anshoo Arora (Relevant Codes) | Copyrights licensed under the New BSD License | http://opensource.org/licenses/BSD-3-Clause
			Documentation: http://extentreports.relevantcodes.com 
		-->
		<meta name='robots' content='noodp, noydir,nofollow' />
		<meta name='editor' content='Sachin Kumar'>
		<meta charset='UTF-8' /> 
		<meta name='description' content='ExtentReports (by Anshoo Arora) is a reporting library for automation testing for .NET and Java. It creates detailed and beautiful HTML reports for modern browsers. ExtentReports shows test and step summary along with dashboards, system and environment details for quick analysis of your tests.' />
		<meta name='viewport' content='width=device-width, initial-scale=1' />
		<title>SEOBOX Report</title>
		<link href="http://fonts.googleapis.com/css?family=Inconsolata" rel="stylesheet" type="text/css">
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">	
		<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link href='https://cdn.rawgit.com/sachinkmr/Content/b9b3378eb9c97032a4861819f11ae30948101c27/SEOBOX/css/extent-seo.css' type='text/css' rel='stylesheet' />	
		<style type="text/css">
			html,body {
				text-rendering: optimizeLegibility !important;
				-webkit-font-smoothing: antialiased !important;
			}
			html, html a {
			    text-shadow: 1px 1px 1px rgba(0,0,0,0.004);
			}
			div.card-panel div#percentage-block{
				position: relative;
			}
			
			div.card-panel div#percentage-block span.pass-percentage.panel-lead{
				position: absolute;
    			top: 30%;
    			left: 55%;
			}
			div#pageSpeedModal div.modal-content #resources .left table td{
				border: none!important;
				border-bottom:0px solid #ddd !important;
			}
			.container {
    			padding-left: 0px; 
			}
			.report-info{
				display:block;
			}
			div#pageSpeedModal div.modal-content #resources .left table td{
                border: none!important;
                border-bottom:0px solid #ddd !important;
            }
			
            #shouldFix h6, #passedInsight h6{
				width:100%;
				padding: 10px;
				background: #333333;
				border-radius: 4px;
			}
			#shouldFix a, #passedInsight a{
				background-color: transparent ;
				color: #039be5 ;
				width:100%;
			}
			h6.google-heading >a{
				color:white !important;
			}
			h6.google-heading {
				color:white;
			}
			.google-info{
				padding: 5px 15px;
				padding-bottom: 20px;
			}
			.outer{
				margin: 4px 0;
			}
			div#pageSpeedModal div.modal-content #shouldFix .google-info ul{
				list-style-type:disc;
				padding: 0 30px;
				margin:2px auto;
			}
			div#pageSpeedModal div.modal-content #shouldFix .google-info ul li{
				list-style-type:disc;
			}
			.loading {
			   font-size:xx-large;
			    position: fixed;
			    width: 100%;
			    height: 100%;
			    background: black;
			    opacity: .78; 
			    display:none;
			    z-index:1000000;
			    color:white;
			    
			}
			.loading>#error {
				margin: 100px 225px;
			    padding: 0 0 17px 0;
			    border-bottom: white dotted;
			}
			.test-steps{
				overflow-x: scroll;
			}
          </style>
	</head>
	
	<body class='extent default hide-overflow' onload="_updateCurrentStage(-1)">
	<div class="loading style-2 error">
	    <div id='error'></div>
	</div>
	
		<header>			
			<div class="blue darken-2 report-info">
				<div class='report-name left'>SEOBOX</div>
				<!-- nav -->
				<nav>			
					<ul id='slide-out' class='right'>
						<li class='analysis waves-effect active'>
							<a href='#!' onclick="_updateCurrentStage(-1)" class='dashboard-view'><i class='mdi-action-track-changes'></i></i> Dashboard</a>
						</li>
						<li class='analysis waves-effect'><a href='#!' class='categories-view' onclick="_updateCurrentStage(1)"><i class='mdi-maps-local-offer'></i> Test Categories</a></li>
						<li class='analysis waves-effect'><a href='#!' class='test-view' onclick="_updateCurrentStage(0)"><i class='mdi-action-dashboard'></i> Test Cases</a></li>				
					</ul>			
				</nav>
		<!-- /nav -->		
			</div>	
					
		</header>
		
		
		
		<!-- container -->
		<div class='container'>
			
			<!-- dashboard -->
			<div id='dashboard-view' class='row'>
				<div class='time-totals'>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-tests'> 
							<span class='panel-name'><b>Total Tests</b></span> 
							<span class='total-tests'> <span class='panel-lead'>${dashboard.totalTests}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-steps'> 
							<span class='panel-name'><b>Total Steps</b></span> 
							<span class='total-steps'> <span class='panel-lead'>${dashboard.totalSteps}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-current'> 
							<span class='panel-name'><b>Test Case Execution Time</b></span> 
							<span class='suite-total-time-current-value panel-lead'>${dashboard.runDuration}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-overall'> 
							<span class='panel-name'><b>Total Time Taken (Overall)</b></span> 
							<span class='suite-total-time-overall-value panel-lead'>${dashboard.runDurationOverall}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-start-time'>
						<div class='card accent green-accent'> 
							<span class='panel-name'><b>Report Start Time:</b></span> 
							<span class='panel-lead suite-started-time'>${dashboard.startedTime?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-end-time'>
						<div class='card accent pink-accent'> 
							<span class='panel-name'><b>Report End Time:</b></span> 
							<span class='panel-lead suite-ended-time'>${.now?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
				</div>
				<div class='charts'>
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>Test Cases View</b></span>
							</div> 							
							<div class='chart-box'>
								<canvas class='text-centered' id='test-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Passed: <span class='t-pass-count weight-normal'>${dashboard.passedTests}</span> test(s)</span>
							</div> 
							<div>
								<span class='weight-light'>Failed: <span class='t-fail-count weight-normal'>${dashboard.failedTests+dashboard.fatalTests+dashboard.errorTests}</span> test(s)</span>
							</div> 
							<div>
								<span class='weight-light'>Others: <span class='t-others-count weight-normal'>${dashboard.otherTests}</span> test(s)</span>
							</div> 
						</div> 
					</div> 
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>Test Steps View</b></span>
							</div> 							 
							<div class='chart-box'>
								<canvas class='text-centered' id='step-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Passed: <span class='s-pass-count weight-normal'></span>${dashboard.passedSteps} step(s)</span>
							</div> 
							<div>							
								<span class='weight-light'>Failed: <span class='s-fail-count weight-normal'></span>${dashboard.failedSteps+dashboard.fatalSteps+dashboard.errorSteps} step(s)</span>
							</div> 
							<div>
								<span class='weight-light'>Others: <span class='s-others-count weight-normal'></span>${dashboard.otherSteps} step(s)</span>
							</div> 
						</div> 
					</div>
					<div class='col s12 m12 l4 fh'> 
						<div class='card-panel'> 
							<span class='panel-name'><b>Pass Percentage</b></span> 
							<div id='percentage-block'>								
								<canvas class="text-centered" id='percentage'></canvas>
								<span class='pass-percentage panel-lead'></span>
							</div>
							<div class='progress light-blue lighten-3'> 
								<div class='determinate light-blue'></div> 
							</div> 
						</div> 
					</div>
				</div>				
					<div class='category-summary-view'>
						<div class='col l8 m6 s12'>
							<div class='card-panel'>
								<span class='label info outline right'><b>Categories</b></span>
								<table class="striped">
									<thead>
										<tr>
											<th>CATEGORY NAME</th>
											<th>Passed</th>
											<th>Failed</th>
											<th>TOTAL TESTS</th>
										</tr>										
									</thead>
									<tbody>
										<#list dashboard.dashBoardCategories?sort as category>
											<tr>
												<td>
													${category.name}
												</td>												
												<td>
													${category.passed}
												</td>
												<td>													
													${category.failed}
												</td>
												<td>
													${category.total}
												</td>
											</tr>
										</#list>
									</tbody>
								</table>
							</div>
						</div>
					</div>	
				<div class='system-view'>
					<div class='col l4 m12 s12'>
						<div class='card-panel'>
							<span class='label info outline right'><b>Environment</b></span>
							<table class="striped">
								<thead>
									<tr>
										<th>PARAM</th>
										<th>VALUE</th>
									</tr>
								</thead>
								<tbody>
								<#list dashboard.systemInfo as key, value>
										<tr>
											<td>${key}</td>
											<td>${value}</td>
										</tr>
									</#list>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
			</div>
			<!-- /dashboard -->
			
			<!-- tests -->
			<div id='test-view' class='row _addedTable'>
				<div class='col _addedCell1'>
					<div class='contents'>
						<div class='card-panel heading'>
							<h5>Tests</h5>
						</div>
						<div class='card-panel filters'>
							<div>
								<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey tests-toggle' data-activates='tests-toggle' data-constrainwidth='true' data-beloworigin='true' data-hover='true' href='#'>
									<i class='mdi-action-reorder'></i>
								</a>
								<ul id='tests-toggle' class='dropdown-content'>
									<li class='pass'><a href='#!'>Pass</a></li>
									<li class='fail'><a href='#!'>Fail</a></li>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.FATAL)>
										<li class='fatal'><a href='#!'>Fatal</a></li>
									</#if>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.ERROR)>
										<li class='error'><a href='#!'>Error</a></li>
									</#if>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.WARNING)>
										<li class='warning'><a href='#!'>Warning</a></li>
									</#if>	
									<li class='skip'><a href='#!'>Skip</a></li>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.UNKNOWN)>
										<li class='unknown'><a href='#!'>Unknown</a></li>
									</#if>	
									<li class='divider'></li>
									<li class='clear'><a href='#!'>Clear Filters</a></li>
								</ul>
							</div>
								<div>
									<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey category-toggle' data-activates='category-toggle' data-constrainwidth='false' data-beloworigin='true' data-hover='true' href='#'>
										<i class='mdi-maps-local-offer'></i>
									</a>
									<ul id='category-toggle' class='dropdown-content'>
										<#list dashboard.dashBoardCategories?sort as category>
											<li class='${category.name}'><a href='#!'>${category.name}</a></li>
										</#list>
										<li class='divider'></li>
										<li class='clear'><a href='#!'>Clear Filters</a></li>
									</ul>
								</div>
							<div>
								<a class='btn-floating btn-small waves-effect waves-light grey' id='clear-filters' alt='Clear Filters' title='Clear Filters'>
									<i class='mdi-navigation-close'></i>
								</a>
							</div>
							<div>
								<a class='btn-floating btn-small waves-effect waves-light blue enabled' id='refreshCharts' alt='Refresh Charts on Filters' title='Refresh Charts on Filters'>
									<i class='mdi-navigation-refresh'></i>
								</a>
							</div>
							<div class='search' alt='Search Tests' title='Search Tests'>
								<div class='input-field left'>
									<input id='searchTests' type='text' class='validate' placeholder='Search Tests...'>
								</div>
								<a href="#" class='btn-floating btn-small waves-effect waves-light grey'>
									<i class='mdi-action-search'></i>
								</a>
							</div>
						</div>
						<div class='card-panel no-padding-h no-padding-v no-margin-v'>
							<div class='wrapper'>
								<ul id='test-collection' class='test-collection'>
									<#list dashboard.testCases?sort as test>										
										<li class='collection-item test displayed active ${test.status}' extentid='${test.id?string}'>
											<div class='test-head'>
												<span class='test-name'>${test.name}</span>
												<span class='test-status label right outline capitalize ${test.status}'>${test.status}</span>
												<span class='category-assigned hide <#list test.cats as category> ${category?lower_case?replace(".", "")?replace("#", "")?replace(" ", "")}</#list>'></span>
											</div>
											<div class='test-body'>
												<div class='test-info'>
													<div class='test-info-pane1'>
														<div title='Test started time' alt='Test started time' class='test-started-time'><b>Start Time: </b>${test.time?datetime?string(dateTimeFormat)}</div>
														<div title='Test ended time' alt='Test ended time' class='test-ended-time'><b>End Time: </b><#if test.endedTime??>${test.endedTime?datetime?string(dateTimeFormat)}</#if></div>
														<div title='Time taken to finish' alt='Time taken to finish' class='test-time-taken'><b>Execution Time: </b><#if test.endedTime??>${test.getRunDuration()}</#if></div>
														<div class='test-desc'><b>Test Case Description:</b> ${test.desc}</div>
														<div class='test-attributes'>
															<#if test.cats?? && test.cats?size != 0>
																<div class='categories'>
																<b>Categories: </b>
																	<#list test.cats as category>
																		<span class='category text-white'>${category}</span>
																	</#list>
																</div>
															</#if>
														</div>
													</div>
												</div>												
												<div class='test-steps'>
													<table class='bordered table-results striped'>
														<thead>
															<tr>
																<th>Status</th>
																<th>TimeStamp</th>
																<th>StepInfo</th>																
																<th>Details</th>
															</tr>
														</thead>
														<tbody>													
														</tbody>
													</table>	
													<button id="loadMore" class="waves-effect waves-light btn hide loadMore" style="margin:20px auto;" data-clickable="false">Load More Results</button>
												<#--	<a id="loadMore" class="waves-effect waves-light btn hide" idval="" style="margin:20px auto;">Load More Results</a> -->
												</div>
											</div>
										</li>
									</#list>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div id='test-details-wrapper' class='col _addedCell2'>
					<div class='contents'>
						<div class='card-panel details-view'>
							<h5 class='details-name'></h5>
							<div class='step-filters right'>
								<span class='btn-floating btn-small waves-effect waves-light blue' status='info' alt='info' title='info'><i class='mdi-action-info-outline'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light green' status='pass' alt='pass' title='pass'><i class='mdi-action-check-circle'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red' status='fail' alt='fail' title='fail'><i class='mdi-navigation-cancel'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red darken-4' status='fatal' alt='fatal' title='fatal'><i class='mdi-navigation-cancel'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red lighten-2' status='error' alt='error' title='error'><i class='mdi-alert-error'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light orange' alt='warning' status='warning' title='warning'><i class='mdi-alert-warning'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light cyan' status='skip' alt='skip' title='skip'><i class='mdi-content-redo'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light grey darken-2' status='clear-step-filter' alt='Clear filters' title='Clear filters'><i class='mdi-content-clear'></i></span>
							</div>
							<div class='details-container'>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /tests -->
			
			<!-- categories -->			
				<div id='categories-view' class='row _addedTable hide'>
					<div class='col _addedCell1'>
						<div class='contents'>
							<div class='card-panel heading'>
								<h5>Categories</h5>
							</div>
							<div class='card-panel filters'>
								<div class='search' alt='Search tests' title='Search tests'>
									<div class='input-field left'>
										<input id='searchTests' type='text' class='validate' placeholder='Search...'>
									</div>
									<a href="#" class='btn-floating btn-small waves-effect waves-light blue lighten-1'>
										<i class='mdi-action-search'></i>
									</a>
								</div>
							</div>
							<div class='card-panel no-padding-h no-padding-v'>
								<div class='wrapper'>
									<ul id='cat-collection' class='cat-collection'>
										<#list dashboard.dashBoardCategories?sort as category>	
											<#assign others = category.total-(category.passed+category.failed)>
											<li class='category-item displayed'>
												<div class='cat-head'>
													<span class='category-name'>${category.name}</span>
												</div>
												<div class='category-status-counts'>
													<#if (category.passed > 0)>
														<span class='pass label dot'>Pass: ${category.passed}</span>
													</#if>
													<#if (category.failed > 0)>
														<span class='fail label dot'>Fail: ${category.failed}</span>
													</#if>
													<#if (others > 0)>
														<span class='other label dot'>Others: ${others}</span>
													</#if>
												</div>
												<div class='cat-body'>
													<div class='category-status-counts'>
														<div class='button-group'>
															<a href='#!' class='pass label filter'>Pass <span class='icon'>${category.passed}</span></a>
															<a href='#!' class='fail label filter'>Fail <span class='icon'>${category.failed}</span></a>
															<a href='#!' class='other label filter'>Others <span class='icon'>${others}</span></a>
														</div>
													</div>
													<div class='cat-tests'>
														<table class='bordered striped'>
															<thead>
																<tr>
																	<th>RunDate</th>
																	<th>Test Name</th>
																	<th>Status</th>
																</tr>
															</thead>
															<tbody>
																<#list category.testCases?sort as test>
																	<tr class='${test.status}'>
																		<td>${test.time?datetime?string(dateTimeFormat)}</td>
																		<td><span class='category-link linked' extentid='${test.id?string}'>${test.name}</span></td>
																		<td><div class='status label capitalize ${test.status}'>${test.status}</div></td>
																	</tr>
																</#list>
															<tbody>
														</table>
													</div>
												</div> 
											</li>
										</#list>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id='cat-details-wrapper' class='col _addedCell2'>
						<div class='contents'>
							<div class='card-panel details-view'>
								<h5 class='cat-name'></h5>
								<div class='cat-container'>
								</div>
							</div>
						</div>
					</div>
				</div>
			<!-- /categories -->
			
			
			<footer id='report-footer'>
				Created By <a href='https://github.com/sachinkmr'>Sachin Kumar</a>. Suite uses <a href='http://extentreports.relevantcodes.com/'>Extent Reports</a> and <a href='https://github.com/yasserg/crawler4j'>Crawler4j</a>.
			</footer>
		</div>
		<div id='testDataCount'>
			<input type='hidden' id='totalTests' name='totalTests' value='${dashboard.totalTests}'>
			<input type='hidden' id='passedTests' name='passedTests' value='${dashboard.passedTests}'>
			<input type='hidden' id='failedTests' name='failedTests' value='${dashboard.failedTests}'>
			<input type='hidden' id='fatalTests' name='fatalTests' value='${dashboard.fatalTests}'>
			<input type='hidden' id='warningTests' name='warningTests' value='${dashboard.warningTests}'>
			<input type='hidden' id='errorTests' name='errorTests' value='${dashboard.errorTests}'>
			<input type='hidden' id='skippedTests' name='skippedTests' value='${dashboard.skippedTests}'>
			<input type='hidden' id='unknownTests' name='unknownTests' value='${dashboard.unknownTests}'>
			<input type='hidden' id='totalSteps' name='totalSteps' value='${dashboard.totalSteps}'>
			<input type='hidden' id='passedSteps' name='passedSteps' value='${dashboard.passedSteps}'>
			<input type='hidden' id='failedSteps' name='failedSteps' value='${dashboard.failedSteps}'>
			<input type='hidden' id='fatalSteps' name='fatalSteps' value='${dashboard.fatalSteps}'>
			<input type='hidden' id='warningSteps' name='warningSteps' value='${dashboard.warningSteps}'>
			<input type='hidden' id='errorSteps' name='errorSteps' value='${dashboard.errorSteps}'>
			<input type='hidden' id='infoSteps' name='infoSteps' value='${dashboard.infoSteps}'>
			<input type='hidden' id='skippedSteps' name='skippedSteps' value='${dashboard.skippedSteps}'>
			<input type='hidden' id='unknownSteps' name='unknownSteps' value='${dashboard.unknownSteps}'>
			<input type='hidden' id='otherTests' name='otherTests' value='${dashboard.otherTests}'>
			<input type='hidden' id='otherSteps' name='otherSteps' value='${dashboard.otherSteps}'>
			<input type='hidden' id='pageNo' name='pageNo' value='0'>
			<input type='hidden' id='report' name='report' value='${dashboard.reportName}'>
			<input type='hidden' id='dbName' name='dbName' value='${dashboard.dbName}'>
			<input type='hidden' id='dbHost' name='dbHost' value='${dashboard.dbHost}'>
			<input type='hidden' id='dbPort' name='dbPort' value='${dashboard.dbPort}'>
			<input type='hidden' id='webAppName' name='webAppName' value='${dashboard.webAppName}'>
			<input type='hidden' id='webAppHost' name='webAppHost' value='${dashboard.webAppHost}'>
			<input type='hidden' id='webAppPort' name='webAppPort' value='${dashboard.webAppPort}'>
			<input type='hidden' id='webApp' name='webApp' value='${dashboard.webApp}'>			
			
		</div>
		<div id="pageSpeedModal" class="modal modal-fixed-footer">
            <div class="modal-content">
                <h4>Google PageSpeed Insights </h4>
                <div class="accordionDiv">
                    <h3> Page Resources</h3>
                    <div>
                        <div id='resources'>						
                            <div class='left' style="width:49%" ><table class="bordered"></table></div>
                            <div class='right'  style="width:49%" >
                                <div>
                                    <b>Resources destribution on page:</b><br/>							
                                </div>
                                <img alt='Page Resources' width='100%'/>
                            </div>
                        </div>
                    </div>
                    <h3> Should Fix</h3>
                    <div>
						<div>
							<b>Following Rules are marked as failed. Click to view more.<br/></b>
						</div>
                        <div id='shouldFix' data-collapse="accordion">						

                        </div>
                    </div>
                    <h3> Passed</h3>
                    <div>
						<div>
							<b>Following Rules has been passed. Click to view more.<br/></b>
						</div>
                        <div id='passedInsight' data-collapse="accordion">						
							
                        </div>
                    </div>				  
                </div>
            </div>
            <div class="modal-footer">
                <a href="#!" class=" modal-action modal-close waves-effect waves-green btn-flat"><strong>Close</strong></a>
            </div>
        </div>
        <div id="pageStructureModal" class="modal modal-fixed-footer">
            <div class="modal-content">
                <h4>Google Page Structured Report</h4>			  
                <p> </p>
            </div>
            <div class="modal-footer">
                <a href="#!" class=" modal-action modal-close waves-effect waves-green btn-flat"><strong>Close</strong></a>
            </div>
        </div>
		<!--
		<script src='https://cdn.rawgit.com/sachinkmr/Content/feec8a14753fa1f227e723c5b9796566c0da03bf/SEOBOX/js/extent-web.js' type='text/javascript'></script>
		<script src='https://cdn.rawgit.com/anshooarora/extentreports/ab0f4299b133bfa234cec0b1e0ac08a692a7640a/cdn/extent.js' type='text/javascript'></script>
		
		-->
		<script src="https://code.jquery.com/jquery-2.2.0.min.js"   integrity="sha256-ihAoc6M/JPfrIiIeayPE9xjin4UWjsx2mjW/rtmxLM4="   crossorigin="anonymous"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>		
		<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.1/Chart.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/featherlight/1.3.4/featherlight.min.js' type='text/javascript'></script>		
		
		<script src='https://cdn.rawgit.com/sachinkmr/Content/03605f09c643fe4943c775b1c5169656dccbdf0a/SEOBOX/js/seobox.js' type='text/javascript'></script>
		<script>		
			if($('.system-view>div>div.card-panel').css('height')>$('.category-summary-view>div>div.card-panel').css('height')){
				$('.category-summary-view>div >div.card-panel').css('height',$('.system-view>div> div.card-panel').css('height'));
			}
			$(document).ready(function() {
			  $('.modal-trigger').leanModal();
			  $(".accordionDiv").accordion({
		            collapsible: true,
		            heightStyle: "content"
		        });
			});
		</script>
	</body>
</html>
