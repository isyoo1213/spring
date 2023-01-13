package spring.upload.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.upload.domain.Item;
import spring.upload.domain.ItemRepository;
import spring.upload.domain.UploadFile;
import spring.upload.file.FileStore;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        //attachFile 가져오기
        MultipartFile getAttachFile = form.getAttachFile();
        UploadFile attachFile = fileStore.storeFile(getAttachFile);

        //imageFiles 가져오기
        List<MultipartFile> getImageFiles = form.getImageFiles();
        List<UploadFile> storeImageFiles = fileStore.storeFiles(getImageFiles);

        //데이터베이스에 저장
        // * 주로 db에 저장하는 것은 파일들의 이름, 상대 경로 정도
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    @GetMapping("items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);

        return "item-view";
    }

}
