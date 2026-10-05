package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Category_24133058;
import com.example.demo.model.Video_24133058;
import com.example.demo.service.VideoService_24133058;

@Controller
public class VideoController_24133058 {

    @Autowired
    private VideoService_24133058 videoService;

    @GetMapping("/video/detail")
    public String videoDetail(@RequestParam("id") String videoId, Model model) {
        Video_24133058 video = videoService.findById(videoId);
        model.addAttribute("v", video);
        return "web/video-detail";
    }

    @GetMapping("/videos")
    public String listVideosByCategory(@RequestParam(value = "categoryId", defaultValue = "1") int categoryId,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       Model model) {
        int pageSize = 3; 
        int totalVideos = videoService.countByCategoryId(categoryId);
        int totalPages = (int) Math.ceil((double) totalVideos / pageSize);

        List<Video_24133058> videos = videoService.findByCategoryIdPage(categoryId, page, pageSize);
        Category_24133058 currentCat = videoService.findCategoryById(categoryId);
        List<Category_24133058> allCategories = videoService.findAllCategoriesWithCount();

        model.addAttribute("videos", videos);
        model.addAttribute("currentCat", currentCat);
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("totalVideos", totalVideos); 
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("categoryId", categoryId);

        return "web/video-list";
    }
}