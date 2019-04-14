<#import "parts/HTMLshell.ftl" as shell> 
<#import "/parts/utils/showPics.ftl" as picture>
<@shell.htmlPage> 

<#include "/parts/security.ftl">

<div class="col-9">
	<a href="/createChat" class="btn btn-primary btn-block" role="button">create new chat</a>
	<#list chatSessions as session>
		<#assign chat = session.connectedChat
				 newMessagesCount = DateUtills.calculateNewMessages(chat.chatMessages, session.lastView)>
<!-- 		<#if DateUtills.isLater(session.connectChat, chat.lastMessageDate)> -->
<!-- 			lul -->
<!-- 		<#else> -->
<!-- 			zulul -->
<!-- 		</#if> -->
		<ul class="list-group">
		  <li class="list-group-item d-flex justify-content-between align-items-center mt-2 shadow" style="border-left: none;
																								 		   border-right: none;
																								 		   border-top: none">
			   <div class="border-secondary media">
			  		<@picture.pic chat "mediumPic" "chatList" />
					<div class="media-body">
						<a href="/chats/${chat.id}" class="h4 ml-2">${chat.chatName}</a>
						<div class="ml-2">
							${chat.chatTitle!"no title"}
						</div>
					</div>
				</div>
				<#if newMessagesCount gt 0>
					<span class="badge badge-light primary">${newMessagesCount}</span>
				</#if>
			</li>
		</ul>
	<#else>
		<h4 class="display-4 ml-2 mt-3 " align="center">No chats :(</h4>
	</#list>
</div>

</@shell.htmlPage>