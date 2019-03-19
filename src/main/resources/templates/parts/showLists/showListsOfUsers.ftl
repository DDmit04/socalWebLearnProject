<#macro showUsers usersList listType>

	<#if listType == "groupOwner">
		<#assign user = usersList >
		<#include "/parts/showLists/showUser.ftl" >
	<#else>
		<#list usersList as user>
			<#include "/parts/showLists/showUser.ftl" >
		<#else>
			<#if listType == "friends">
				<h4 class="display-4 ml-2 " align="left">No friends :(</h4>			
			<#elseif listType == "subscriptions">
				<h4 class="display-4 ml-2 " align="left">No subscriptions :(</h4>
			<#elseif listType == "groupSubscriptions" || listType == "subscribers">
				<h4 class="display-4 ml-2 " align="left">No subscribers :(</h4>
			<#elseif listType == "groupAdmins">
				<h4 class="display-4 ml-2 " align="left">No admins :(</h4>
			<#else>
				<h4 class="display-4 ml-2 " align="left">Empty List</h4>
			</#if>
		</#list>
	</#if>
		
</#macro>