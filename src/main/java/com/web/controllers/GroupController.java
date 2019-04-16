package com.web.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.web.data.Group;
import com.web.data.User;
import com.web.data.dto.GroupDto;
import com.web.data.dto.PostDto;
import com.web.data.dto.UserDto;
import com.web.exceptions.GroupException;
import com.web.service.GroupService;
import com.web.service.PostService;
import com.web.service.UserService;

@Controller
public class GroupController {
	
	@Autowired
	private GroupService groupService;
		
	@Autowired
	private UserService userServive;
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/groups")
	public String getAllGroups(@AuthenticationPrincipal User currentUser,
							   Model model) {
		Iterable<GroupDto> allGroups = groupService.findAllGroupsDto();
		Iterable<GroupDto> userGroups = groupService.findUserGroupsDto(currentUser);
		model.addAttribute("allGroups", allGroups);
		model.addAttribute("userGroups", userGroups);
		model.addAttribute("userGroupsCount", userGroups.spliterator().getExactSizeIfKnown());
		return "groupList";
	}
	
	@GetMapping("/groups/{group}")
	public String getGroup(@AuthenticationPrincipal User currentUser,
						   @PathVariable Group group,
						   Model model) {
		GroupDto oneGroup = groupService.findOneGroup(group, currentUser);
		UserDto oneUser = userServive.findOneUserToGroup(currentUser, group);
		Iterable<PostDto> groupPosts = postService.findGroupPosts(currentUser, group);
		model.addAttribute("posts", groupPosts);
		model.addAttribute("group", oneGroup);
		model.addAttribute("groupSubs", group.getGroupSubs());
		model.addAttribute("user", oneUser);
		return "group";
	}
	
	@PostMapping("/groups/{group}")
	public String addGroupPost(@AuthenticationPrincipal User currentUser,
							   @PathVariable Group group,
							   @RequestParam String postText,
							   @RequestParam String tags,
							   @RequestParam("file") MultipartFile file,
							   Model model) throws IllegalStateException, IOException {
		postService.addPost(postText, tags, file, group);
		return "redirect:/groups/" + group.getId();
	}
	
	@GetMapping("{user}/groups/create")
	public String createGroup() {
		return "createGroup";
	}
	
	@PostMapping("{user}/groups/create")
	public String createGroup(@AuthenticationPrincipal User currentUser,
							  @RequestParam String groupName,
							  @RequestParam (required = false)String groupTitle,
							  @RequestParam (required = false)String groupInformation,
							  @RequestParam("file") MultipartFile file,
							  Model model) throws IllegalStateException, IOException {
		Group group;
		try {
			group = groupService.createGroup(groupName, groupInformation, groupTitle, file, currentUser);
		} catch (GroupException e) {
			model.addAttribute("groupNameError", e.getMessage());
			model.addAttribute("registrationName", e.getGroup().getGroupName());
			return "createGroupForm";
		}
		return "redirect:/groups/" + group.getId();
	}
	
	@GetMapping("/groups/{group}/{user}/sub")
	public String subGroup(@AuthenticationPrincipal User currentUser,
						   @PathVariable User user,
						   @PathVariable Group group) {
		if(user.equals(currentUser)) {
			groupService.addGroupSub(group, user); 
		}
		return "redirect:/groups/" + group.getId();
	}
	
	@GetMapping("/groups/{group}/{user}/unsub")
	public String unsubGroup(@AuthenticationPrincipal User currentUser,
							 @PathVariable User user,
							 @PathVariable Group group) {
		if(user.equals(currentUser)) {
			groupService.removeGroupSub(user, group);
		}
		return "redirect:/groups/" + group.getId();
	}
	
	@GetMapping("/groups/{group}/{user}/addAdmin")
	public String addAdmin(@AuthenticationPrincipal User currentUser,
						   @PathVariable Group group,
						   @PathVariable User user) {
		
		groupService.addGroupAdmin(currentUser, user, group);
		return "redirect:/groups/" + group.getId() + "/socialList/groupAdmins";
	}
	
	@GetMapping("/groups/{group}/{user}/removeAdmin")
	public String removeAdmin(@AuthenticationPrincipal User currentUser,
							  @PathVariable Group group,
							  @PathVariable User user) {
		
		groupService.removeGroupAdmin(currentUser, user, group);
		return "redirect:/groups/" + group.getId() + "/socialList/groupAdmins";
	}
	
	@GetMapping("/groups/{group}/{user}/ban")
	public String banUserInGroup(@AuthenticationPrincipal User currentUser,
						  		 @PathVariable Group group,
						  		 @PathVariable User user) {
		groupService.banUser(currentUser, user, group);
		return "redirect:/groups/" + group.getId() + "/socialList/groupBanList";
	}
	
	@GetMapping("/groups/{group}/{user}/unban")
	public String unbanUserInGroup(@AuthenticationPrincipal User currentUser,
					 	 		   @PathVariable Group group,
					 	 		   @PathVariable User user) {
		groupService.unbanUser(currentUser, user, group);
		return "redirect:/groups/" + group.getId() + "/socialList/groupBanList";
	}
	
	@GetMapping("/groups/{group}/{user}/makeOwner")
	public String makeGroupOwnerAuth(Model model) {
		model.addAttribute("loginAttention", "confirm the action on the group with login and password");
		return "login";
	}
	
	@PostMapping("/groups/{group}/{user}/makeOwner")
	public String makeGroupOwner(@AuthenticationPrincipal User currentUser,
							 	 @RequestParam String username,
							 	 @RequestParam String password,
							 	 @PathVariable Group group,
							 	 @PathVariable User user) {
		//Password encoder!!!
		User groupOwner = group.getGroupOwner();
		if(groupOwner.equals(currentUser) 
				&& username.equals(groupOwner.getUsername()) 
				&& password.equals(groupOwner.getPassword())) {
			groupService.makeOwner(group, user);
		}
		return "redirect:/groups/" + group.getId() + "/socialList/groupAdmins";
	}
	
	@GetMapping("/groups/{group}/socialList/{listType}")
	public String groupConnections(@AuthenticationPrincipal User currentUser,
								   @PathVariable String listType,
							 	   @PathVariable Group group,
							 	   Model model) {
		GroupDto currentGroup = groupService.findOneGroup(group, currentUser);
		model.addAttribute("group", currentGroup);
		model.addAttribute("groupSubscrabers", group.getGroupSubs());
		model.addAttribute("groupAdmins", group.getGroupAdmins());
		model.addAttribute("groupBanList", group.getGroupBanList());
		model.addAttribute("listType", listType);
		return "groupConnections";
	}
	
}