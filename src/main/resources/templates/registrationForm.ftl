<#import "parts/HTMLshell.ftl" as shell> <@shell.htmlPage> 

<div class="text-center">
<h1 class="h3 mb-3 font-weight-normal">Registration</h1>
</div>
<form method="post" action="/registration">
	<div class="form-group">
	    <label for="exampleInputEmail1">Username:</label>
		<input class="form-control mb-2" type="text" name="username" placeholder="enter username">
	</div>
	<div class="form-group">
	    <label for="exampleInputEmail1">Passowd:</label>
		<input class="form-control mb-2" type="password" name="password" placeholder="enter password">
	</div>
	<button class="btn btn-primary" type="submit">create user</button>
	<input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>

</@shell.htmlPage> 
