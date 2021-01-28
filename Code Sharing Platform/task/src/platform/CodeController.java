package platform;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
public class CodeController {

    @JsonSerialize
    public class EmptyJsonResponse { }
    Integer idOfCode = 0;
    Map<Integer, Code> dataBase = new TreeMap<>();

    @GetMapping(value = "/code/new")
    public String sendNewCode() {
        return "new_code";
    }

    @PostMapping(value = "/api/code/new", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String newCode(@RequestBody Map<String,String> newCode) {
        Code code = new Code();
        code.setCode(newCode.get("code"));
        code.setDateOfUpdate();
        idOfCode++;
        dataBase.put(idOfCode, code);
        return "{ \"id\" : " + "\"" + idOfCode + "\"" + " }";
    }

    @GetMapping(value = "/code/{id}")
    public String getIndex(@PathVariable Integer id, Model model) {
        if (!dataBase.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such code snippet");
        } else {
            model.addAttribute("load_date", dataBase.get(id).getDate());
            model.addAttribute("load_code", dataBase.get(id).getCode());
            return "index";
        }
    }

    @ResponseBody
    @GetMapping(path = "/api/code/{id}", produces = "application/json")
    private Code getJsonCode(@PathVariable Integer id) {
        System.out.println(id);
        if (!dataBase.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such code snippet");
        } else {
            return dataBase.get(id);
        }
    }

    @GetMapping(value = "/code/latest")
    public String getLatestHtml(Model model) {
        if (!dataBase.containsKey( 1)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such code snippet");
        } else {
            List<Code> latestCodeSnippets = getLatestCodeSnippets(dataBase);
            StringBuilder finalString = new StringBuilder();
            for(int i = 0; i < latestCodeSnippets.size(); i++) {
                String latestCodeSnippetsToString =  "<span>" +
                        latestCodeSnippets.get(i).getDate() + "</span>" +
                        "<pre>" + latestCodeSnippets.get(i).getCode() + "</pre>";
                finalString.append(latestCodeSnippetsToString);
            }
            model.addAttribute("load_code", finalString);
            System.out.println(finalString);
            return "latest";
        }
    }

    @ResponseBody
    @GetMapping(path = "/api/code/latest", produces = "application/json")
    private List<Code> getLatestJson() {
        if (!dataBase.containsKey(1)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dataBase is empty");
        } else {
            return getLatestCodeSnippets(dataBase);
        }
    }

    List<Code> getLatestCodeSnippets(Map<Integer, Code> dataBase) {
        List<Code> latestCodeSnippets = new LinkedList<>();
        int baseSize = dataBase.size();
        if (baseSize > 10) {
            for (int i = baseSize - 9; i <= baseSize; i++) {
                latestCodeSnippets.add(dataBase.get(i));
            }
            Collections.reverse(latestCodeSnippets);
            return latestCodeSnippets;
        } else {
            for (int i = 1; i <= baseSize; i++) {
                latestCodeSnippets.add(dataBase.get(i));
            }
            Collections.reverse(latestCodeSnippets);
            return latestCodeSnippets;
        }
    }
}