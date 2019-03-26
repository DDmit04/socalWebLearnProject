package com.web.controllers;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.web.data.Post;
import com.web.data.User;
import com.web.data.dto.PostDto;
import com.web.data.dto.UserDto;
import com.web.service.PostService;

@Controller
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/")
	public String greetingPage(Model model) {
		return "redirect:/posts";
	}
	
	@GetMapping("/posts")
	public String getPosts(@AuthenticationPrincipal User currentUser,
						   @RequestParam(required = false) String search,
						   Model model) {
		Iterable<PostDto> searchByTag = postService.searchPostsByTag(search, currentUser);
		model.addAttribute("user", currentUser);
		model.addAttribute("posts", searchByTag);
		model.addAttribute("search", search);
		return "postList";
	}
	
	@GetMapping("/subscriptionPosts")
	public String getFriendPosts(@AuthenticationPrincipal User currentUser,
							     @RequestParam(required = false) String search,
							     Model model) {
		Iterable<PostDto> searchFriendPosts = postService.searchSubscriptionsPosts(currentUser);
		model.addAttribute("user", currentUser);
		model.addAttribute("posts", searchFriendPosts);
		model.addAttribute("search", search);
		return "postList";
	}
	
	@PostMapping(value= {"/posts", "/subscriptionPosts"})
	public String addPost(@AuthenticationPrincipal User currentUser,
						  @RequestParam String postText, 
						  @RequestParam String tags,
						  @RequestParam(required = false) String search,
						  @RequestParam("file") MultipartFile file,
						  Model model) throws IllegalStateException, IOException {
		postService.addPost(postText, tags, file, currentUser);
		model.addAttribute("search", search);
		return "redirect:/posts";
	}
	
	@GetMapping("{post}/edit")
	public String getPost(@AuthenticationPrincipal User currentUser,
						  @PathVariable Post post, 
			  			  Model model) {
		PostDto editedPost = postService.findOnePost(currentUser, post);
		model.addAttribute("post", editedPost);
		return "postList";	
	}
	
	@PostMapping("{post}/edit")
	public String editPost(@AuthenticationPrincipal User currentUser,
						   @PathVariable Post post, 
					       @RequestParam String postText,
						   @RequestParam String tags, 
						   @RequestParam("file") MultipartFile file,
						   Model model) throws IllegalStateException, IOException {
		if((post.getPostAuthor() != null && post.getPostAuthor().equals(currentUser))
		|| (post.getPostGroup() != null && post.getPostGroup().getGroupAdmins().contains(currentUser))) {
			postService.updatePost(post, postText, tags, file);
		}
		return "redirect:/posts";
	}
	
	@GetMapping("{post}/delete")
	public String deletePost(@AuthenticationPrincipal User currentUser,
							 @PathVariable Post post) {
		if((post.getPostAuthor() != null && post.getPostAuthor().equals(currentUser))
		|| (post.getPostGroup() != null && post.getPostGroup().getGroupAdmins().contains(currentUser))) {
			postService.deletePost(post, currentUser);
		}
		return "redirect:/posts";
	}
	
	@GetMapping("{post}/removeRepost")
	public String removeRepost(@AuthenticationPrincipal User currentUser,
							   @PathVariable Post post,
			 				   RedirectAttributes redirectAttributes,
			 				   @RequestHeader(required = false) String referer) {
		if((post.getPostAuthor() != null && post.getPostAuthor().equals(currentUser))
		|| (post.getPostGroup() != null && post.getPostGroup().getGroupAdmins().contains(currentUser))) {
			postService.removeRepost(post);
		}
		return "redirect:/" + post.getId() + "/edit" ;		
	}
	
	@GetMapping("posts/{post}/like")
	public String likePost(@AuthenticationPrincipal User currentUser,
						   @PathVariable Post post,
						   RedirectAttributes redirectAttributes,
						   @RequestHeader(required = false) String referer) {
		Set<User> like = post.getPostLikes();
		if(like.contains(currentUser)) {
			like.remove(currentUser);
		} else {
			like.add(currentUser);
		}
		UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
		components.getQueryParams()
			.entrySet()
			.forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
		return "redirect:" + components.getPath();	
	}
	
	@PostMapping("{repostedPost}/repost")
	public String addRepost(@AuthenticationPrincipal User currentUser,
							@PathVariable Post repostedPost, 
			 				@RequestParam(required = false) String repostText,
			 				@RequestParam String repostTags) throws IllegalStateException, IOException {
		postService.addRepost(currentUser, repostedPost, repostText, repostTags);
		return "redirect:/posts";
	}
	
}