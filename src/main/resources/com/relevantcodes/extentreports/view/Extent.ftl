<#assign dateFormat = "dd-MMMM-YYYY">
<#assign timeFormat = "h:mm:ss a">
<#assign dateTimeFormat = "dd-MMMM-YYYY" + " " + "h:mm:ss a">

<!DOCTYPE html>
<html>
	<head>
		<!--
			ExtentReports ${resourceBundle.getString("head.library")} 2.41.1 | http://relevantcodes.com/extentreports-for-selenium/ | https://github.com/anshooarora/
			Copyright (c) 2015, Anshoo Arora (Relevant Codes) | ${resourceBundle.getString("head.copyrights")} | http://opensource.org/licenses/BSD-3-Clause
			${resourceBundle.getString("head.documentation")}: http://extentreports.relevantcodes.com 
		-->
		<meta charset='UTF-8' /> 
		<meta name='description' content='${resourceBundle.getString("head.metaDescription")}' />
		<meta name='robots' content='noodp, noydir' />
		<meta name='viewport' content='width=device-width, initial-scale=1' />
		<title>SEOBOX Report</title>
		<link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600' rel='stylesheet' type='text/css'>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">	
		<link href='https://cdn.rawgit.com/sachinkmr/Content/603c015f7c5df430482f89ab9e638beb34ffcfd2/SEOBOX/css/extent.css' type='text/css' rel='stylesheet' />	
	</head>
	
	<body class='extent default hide-overflow' onload="_updateCurrentStage(-1)">
		<header>
			<div class='logo-container left'>
				<img src="https://cdn.rawgit.com/sachinkmr/Content/master/SEOBOX/Images/logo.png" alt='SEOBOX' title="SEOBOX" class="responsive-img">
				<a href='#' data-activates='slide-out' class='button-collapse hide-on-large-only'><i class='mdi-navigation-apps'></i></a>
			</div>
			<div class="blue darken-2 report-info">
				<div class='report-name'></div> <div class='report-headline'>Site SEO and Performance Validator</div>				
			</div>			
		</header>
		
		<!-- nav -->
		<nav>			
			<ul id='slide-out' class='side-nav fixed hide-on-med-and-down'>
				<li class='analysis waves-effect active'>
					<a href='#!' onclick="_updateCurrentStage(-1)" class='dashboard-view'><i class='mdi-action-track-changes'></i></i> Dashboard</a>
				</li>
				<li class='analysis waves-effect'><a href='#!' class='categories-view' onclick="_updateCurrentStage(1)"><i class='mdi-maps-local-offer'></i>Test Categories</a></li>
				<li class='analysis waves-effect'><a href='#!' class='test-view' onclick="_updateCurrentStage(0)"><i class='mdi-action-dashboard'></i>Test Cases</a></li>				
			</ul>			
		</nav>
		<!-- /nav -->
		
		<!-- container -->
		<div class='container'>
			
			<!-- dashboard -->
			<div id='dashboard-view' class='row'>
				<div class='time-totals'>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-tests'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.totalTests")}</b></span> 
							<span class='total-tests'> <span class='panel-lead'>${dashboard.totalTests}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-steps'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.totalSteps")}</b></span> 
							<span class='total-steps'> <span class='panel-lead'>${dashboard.totalSteps}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-current'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.totalTimeTaken.current")}</b></span> 
							<span class='suite-total-time-current-value panel-lead'>${report.getRunDuration()}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-overall'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.totalTimeTaken.overall")}</b></span> 
							<span class='suite-total-time-overall-value panel-lead'>${report.getRunDurationOverall()}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-start-time'>
						<div class='card accent green-accent'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.start")}</b></span> 
							<span class='panel-lead suite-started-time'>${report.startedTime?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-end-time'>
						<div class='card accent pink-accent'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.end")}</b></span> 
							<span class='panel-lead suite-ended-time'>${.now?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
				</div>
				<div class='charts'>
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.testsView")}</b></span>
							</div> 							
							<div class='chart-box'>
								<canvas class='text-centered' id='test-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Passed: <span class='t-pass-count weight-normal'>${dashboard.passedTests}</span> ${resourceBundle.getString("dashboard.panel.label.testsPassed")}</span>
							</div> 
							<div>
								<span class='weight-light'>Failed: <span class='t-fail-count weight-normal'>${dashboard.failedTests}</span> ${resourceBundle.getString("dashboard.panel.label.testsFailed")}</span>
							</div> 
							<div>
								<span class='weight-light'>Others: <span class='t-others-count weight-normal'>${dashboard.otherTests}</span> ${resourceBundle.getString("dashboard.panel.label.others")}</span>
							</div> 
						</div> 
					</div> 
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.stepsView")}</b></span>
							</div> 							 
							<div class='chart-box'>
								<canvas class='text-centered' id='step-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Passed: <span class='s-pass-count weight-normal'></span> ${resourceBundle.getString("dashboard.panel.label.stepsPassed")}</span>
							</div> 
							<div>
								<span class='weight-light'>Failed: <span class='s-fail-count weight-normal'></span> ${resourceBundle.getString("dashboard.panel.label.stepsFailed")}</span>
							</div> 
							<div>
								<span class='weight-light'>Others: <span class='s-others-count weight-normal'></span> ${resourceBundle.getString("dashboard.panel.label.others")}</span>
							</div> 
						</div> 
					</div>
					<div class='col s12 m12 l4 fh'> 
						<div class='card-panel'> 
							<span class='panel-name'><b>${resourceBundle.getString("dashboard.panel.name.passPercentage")}</b></span> 
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
								<span class='label info outline right'><b>${resourceBundle.getString("dashboard.panel.name.categories")}</b></span>
								<table>
									<thead>
										<tr>
											<th>${resourceBundle.getString("dashboard.panel.th.catName")}</th>
											<th>Passed</th>
											<th>Failed</th>
											<th>${resourceBundle.getString("dashboard.panel.th.catValue")}</th>
										</tr>										
									</thead>
									<tbody>
										<#list dashboard.dashBoardCategories as category>
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
							<span class='label info outline right'><b>${resourceBundle.getString("dashboard.panel.name.environment")}</b></span>
							<table>
								<thead>
									<tr>
										<th>${resourceBundle.getString("dashboard.panel.th.param")}</th>
										<th>${resourceBundle.getString("dashboard.panel.th.value")}</th>
									</tr>
								</thead>
								<tbody>
									<#list dashboard.systemInfoMap?keys as info>
										<tr>
											<td>${info}</td>
											<td>${dashboard.systemInfoMap[info]}</td>
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
							<h5>${resourceBundle.getString("tests.heading")}</h5>
						</div>
						<div class='card-panel filters'>
							<div>
								<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey tests-toggle' data-activates='tests-toggle' data-constrainwidth='true' data-beloworigin='true' data-hover='true' href='#'>
									<i class='mdi-action-reorder'></i>
								</a>
								<ul id='tests-toggle' class='dropdown-content'>
									<li class='pass'><a href='#!'>Pass</a></li>
									<li class='fail'><a href='#!'>Fail</a></li>
									<#if report.logStatusList?? && report.logStatusList?seq_contains(LogStatus.FATAL)>
										<li class='fatal'><a href='#!'>Fatal</a></li>
									</#if>
									<#if report.logStatusList?? && report.logStatusList?seq_contains(LogStatus.ERROR)>
										<li class='error'><a href='#!'>Error</a></li>
									</#if>
									<#if report.logStatusList?? && report.logStatusList?seq_contains(LogStatus.WARNING)>
										<li class='warning'><a href='#!'>Warning</a></li>
									</#if>	
									<li class='skip'><a href='#!'>Skip</a></li>
									<#if report.logStatusList?? && report.logStatusList?seq_contains(LogStatus.UNKNOWN)>
										<li class='unknown'><a href='#!'>Unknown</a></li>
									</#if>	
									<li class='divider'></li>
									<li class='clear'><a href='#!'>${resourceBundle.getString("tests.filters.clearFilters")}</a></li>
								</ul>
							</div>
								<div>
									<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey category-toggle' data-activates='category-toggle' data-constrainwidth='false' data-beloworigin='true' data-hover='true' href='#'>
										<i class='mdi-maps-local-offer'></i>
									</a>
									<ul id='category-toggle' class='dropdown-content'>
										<#list dashboard.dashBoardCategories as category>
											<li class='${category.name}'><a href='#!'>${category.name}</a></li>
										</#list>
										<li class='divider'></li>
										<li class='clear'><a href='#!'>${resourceBundle.getString("tests.filters.clearFilters")}</a></li>
									</ul>
								</div>
							<div>
								<a class='btn-floating btn-small waves-effect waves-light grey' id='clear-filters' alt='${resourceBundle.getString("tests.filters.clearFilters")}' title='${resourceBundle.getString("tests.filters.clearFilters")}'>
									<i class='mdi-navigation-close'></i>
								</a>
							</div>
							<div>
								<a class='btn-floating btn-small waves-effect waves-light blue enabled' id='refreshCharts' alt='${resourceBundle.getString("tests.filters.refreshCharts")}' title='${resourceBundle.getString("tests.filters.refreshCharts")}'>
									<i class='mdi-navigation-refresh'></i>
								</a>
							</div>
							<div class='search' alt='${resourceBundle.getString("tests.filters.searchTests")}' title='${resourceBundle.getString("tests.filters.searchTests")}'>
								<div class='input-field left'>
									<input id='searchTests' type='text' class='validate' placeholder='${resourceBundle.getString("tests.filters.searchTests")}...'>
								</div>
								<a href="#" class='btn-floating btn-small waves-effect waves-light grey'>
									<i class='mdi-action-search'></i>
								</a>
							</div>
						</div>
						<div class='card-panel no-padding-h no-padding-v no-margin-v'>
							<div class='wrapper'>
								<ul id='test-collection' class='test-collection'>
									<#list report.testList as extentTest>
										<#assign test = extentTest.getTest()>
										<li class='collection-item test displayed active ${test.status}' extentid='${test.id?string}'>
											<div class='test-head'>
												<span class='test-name'>${test.name}</span>
												<span class='test-status label right outline capitalize ${test.status}'>${test.status}</span>
												<span class='category-assigned hide <#list test.categoryList as category> ${category.name?lower_case?replace(".", "")?replace("#", "")?replace(" ", "")}</#list>'></span>
											</div>
											<div class='test-body'>
												<div class='test-info'>
													<div class='test-info-pane1'>
														<div title='${resourceBundle.getString("tests.test.info.testStartTime")}' alt='${resourceBundle.getString("tests.test.info.testStartTime")}' class='test-started-time'><b>Start Time: </b>${test.startedTime?datetime?string(dateTimeFormat)}</div>
														<div title='${resourceBundle.getString("tests.test.info.testEndTime")}' alt='${resourceBundle.getString("tests.test.info.testEndTime")}' class='test-ended-time'><b>End Time: </b><#if test.endedTime??>${test.endedTime?datetime?string(dateTimeFormat)}</#if></div>
														<div title='${resourceBundle.getString("tests.test.info.timeTaken")}' alt='${resourceBundle.getString("tests.test.info.timeTaken")}' class='test-time-taken'><b>Execution Time: </b><#if test.endedTime??>${test.getRunDuration()}</#if></div>
														<div class='test-desc'>${test.description}</div>
														<div class='test-attributes'>
															<#if test.categoryList?? && test.categoryList?size != 0>
																<div class='categories'>
																<b>Categories: </b>
																	<#list test.categoryList as category>
																		<span class='category text-white'>${category.name}</span>
																	</#list>
																</div>
															</#if>
														</div>
													</div>
													<!--<div class='test-info-pane2'>
														<div class="chart-box">
															<canvas class='text-centered' id='test-step-analysis'></canvas>
														</div>
													</div>
													-->
												</div>												
												<div class='test-steps'>
													<table class='bordered table-results'>
														<thead>
															<tr>
																<th>${resourceBundle.getString("tests.test.log.th.status")}</th>
																<th>${resourceBundle.getString("tests.test.log.th.timestamp")}</th>
																<#if (test.logList[0].stepName)??>
																	<th>StepInfo</th>
																</#if>
																<th>${resourceBundle.getString("tests.test.log.th.details")}</th>
															</tr>
														</thead>
														<tbody>
															<#list test.logList as log>
																<tr>
																	<td class='status ${log.logStatus}' title='${log.logStatus}' alt='${log.logStatus}'><i class='${Icon.getIcon(log.logStatus)}'></i></td>
																	<td class='timestamp'>${log.timestamp?datetime?string(timeFormat)}</td>
																	<#if test.logList[0].stepName?? && log.stepName??>
																		<td class='step-name'>${log.stepName}</td>
																	</#if>
																	<td class='step-details'>${log.details}</td>
																</tr>
															</#list>
														</tbody>
													</table>
													<ul class='collapsible node-list' data-collapsible='accordion'>
														<#if test.nodeList?? && test.nodeList?has_content>
															<@recurse_nodes nodeList=test.nodeList depth=1 />
															<#macro recurse_nodes nodeList depth>
																<#list nodeList as node>
																	<li class='displayed ${node.status} node-${depth}x'>
																		<div class='collapsible-header test-node ${node.status}'>
																			<div class='right test-info'>
																				<span title='${resourceBundle.getString("tests.test.info.testStartTime")}' alt='${resourceBundle.getString("tests.test.info.testStartTime")}' class='test-started-time label green lighten-2 text-white'>${node.startedTime?datetime?string(dateTimeFormat)}</span>
																				<span title='${resourceBundle.getString("tests.test.info.testEndTime")}' alt='${resourceBundle.getString("tests.test.info.testEndTime")}' class='test-ended-time label red lighten-2 text-white'>${node.endedTime?datetime?string(dateTimeFormat)}</span>
																				<span title='${resourceBundle.getString("tests.test.info.timeTaken")}' alt='${resourceBundle.getString("tests.test.info.timeTaken")}' class='test-time-taken label blue-grey lighten-2 text-white'>${node.getRunDuration()}</span>
																				<span class='test-status label outline capitalize ${node.status}'>${node.status}</span>
																			</div>
																			<div class='test-node-name'>${node.name}</div>
																			<#if node.description??>
																				<div class='test-node-desc'>${node.description}</div>
																			</#if>
																		</div>
																		<div class='collapsible-body'>
																			<div class='test-steps'>
																				<table class='bordered table-results'>
																					<thead>
																						<tr>
																							<th>${resourceBundle.getString("tests.test.log.th.status")}</th>
																							<th>${resourceBundle.getString("tests.test.log.th.timestamp")}</th>
																							<#if (node.logList[0].stepName)??>
																								<th>StepName</th>
																							</#if>
																							<th>${resourceBundle.getString("tests.test.log.th.details")}</th>
																						</tr>
																					</thead>
																					<tbody>
																						<#list node.logList as log>
																							<tr>
																								<td class='status ${log.logStatus}' title='${log.logStatus}' alt='${log.logStatus}'><i class='${Icon.getIcon(log.logStatus)}'></i></td>
																								<td class='timestamp'>${log.timestamp?datetime?string(timeFormat)}</td>
																								<#if node.logList[0].stepName?? && log.stepName??>
																									<td class='step-name'>${log.stepName}</td>
																								</#if>
																								<td class='step-details'>${log.details}</td>
																							</tr>
																						</#list>
																					</tbody>
																				</table>
																			</div>
																		</div>
																	</li>
																	<@recurse_nodes nodeList=node.nodeList depth=depth+1 />
																</#list>
															</#macro>
														</#if>
													</ul>
													<script></script>
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
								<h5>${resourceBundle.getString("categories.heading")}</h5>
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
										<#list dashboard.dashBoardCategories as category>	
											<#assign others = category.total-(category.passed+category.failed)>
											<li class='category-item displayed'>
												<div class='cat-head'>
													<span class='category-name'>${category}</span>
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
															<a href='#!' class='other label filter'>Others <span class='icon'>${category.others}</span></a>
														</div>
													</div>
													<div class='cat-tests'>
														<table class='bordered'>
															<thead>
																<tr>
																	<th>${resourceBundle.getString("categories.th.runDate")}</th>
																	<th>${resourceBundle.getString("categories.th.testName")}</th>
																	<th>${resourceBundle.getString("categories.th.status")}</th>
																</tr>
															</thead>
															<tbody>
																<#list category.testCases as test>
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
		</div>
		<!-- /container -->
		
		<script src='${protocol}://cdn.rawgit.com/anshooarora/extentreports/6032d73243ba4fe4fb8769eb9c315d4fdf16fe68/cdn/extent.js' type='text/javascript'></script>
		
		-->
		<script   src="https://code.jquery.com/jquery-2.2.0.min.js"   integrity="sha256-ihAoc6M/JPfrIiIeayPE9xjin4UWjsx2mjW/rtmxLM4="   crossorigin="anonymous"></script>
		<script   src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"   integrity="sha256-xNjb53/rY+WmG+4L6tTl9m6PpqknWZvRt0rO1SRnJzw="   crossorigin="anonymous"></script>		
		<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.1/Chart.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/featherlight/1.3.4/featherlight.min.js' type='text/javascript'></script>		
		
		<script src='https://cdn.rawgit.com/sachinkmr/Content/ab0f4299b133bfa234cec0b1e0ac08a692a7640a/SEOBOX/js/extent.js' type='text/javascript'></script>
		<script>		
			if($('.system-view>div>div.card-panel').css('height')>$('.category-summary-view>div>div.card-panel').css('height')){
				$('.category-summary-view>div >div.card-panel').css('height',$('.system-view>div> div.card-panel').css('height'));
			}
			
			<#if report.configurationMap??>
				${report.configurationMap["scripts"]}
			</#if>
		</script>
	</body>
</html>
