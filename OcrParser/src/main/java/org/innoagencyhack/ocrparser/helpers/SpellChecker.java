package org.innoagencyhack.ocrparser.helpers;

import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.text.Word;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;
import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.tools.SpellCheck;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author trushkov
 */
public class SpellChecker {
    private static volatile SpellChecker instance;
    
    private final TokenizerME tokenizer;
    private final NameFinderME nameFinder;
    private final SpellCheck spellCheck;
   
    private static TokenizerME inputStreamTokenizer() throws IOException {
        try(InputStream input = new FileInputStream(new File(SpellChecker.class.getClassLoader().getResource("en-token.bin").getFile()).toString())) {
            TokenizerModel tokenModel = new TokenizerModel(input); 
            return new TokenizerME(tokenModel);
        }
    }
    
    private static NameFinderME inputStreamModel() throws IOException {
        try(InputStream input = new FileInputStream(new File(SpellChecker.class.getClassLoader().getResource("combined.txt.bin").getFile()).toString())) {
            TokenNameFinderModel model = new TokenNameFinderModel(input);
            return new NameFinderME(model);
        }
    }
    
    private SpellChecker() throws SuggesterException, IOException {
        this.tokenizer = inputStreamTokenizer();
        this.nameFinder = inputStreamModel();
        BasicDictionary dictionary1 = new BasicDictionary("file://" + new File(SpellChecker.class.getClassLoader().getResource("russian.jar").getFile()).toString());
        BasicDictionary dictionary2 = new BasicDictionary("file://" + new File(SpellChecker.class.getClassLoader().getResource("my_dictionary.zip").getFile()).toString());
        BasicSuggesterConfiguration configuration = new BasicSuggesterConfiguration("file://" + new File(SpellChecker.class.getClassLoader().getResource("basicSuggester.config").getFile()).toString());
        SpellCheckConfiguration spell_configuration = new SpellCheckConfiguration("file://" + new File(SpellChecker.class.getClassLoader().getResource("spellCheck.config").getFile()).toString());
        BasicSuggester suggester = new BasicSuggester(configuration);
        suggester.attach(dictionary1);
        suggester.attach(dictionary2);
        this.spellCheck = new SpellCheck(spell_configuration);
        this.spellCheck.setSuggester(suggester);
        this.spellCheck.setSuggestionLimit(1);
    }
    
    public static SpellChecker getInstance() throws SuggesterException, IOException, Exception {
        SpellChecker result = instance;
        if (result != null) {
            return result;
        }
        synchronized(SpellChecker.class) {
            if (instance == null) {
                instance = new SpellChecker();
            }
            return instance;
        }
    }
    
    public boolean isCorrectText(String text) throws SuggesterException{
        String tokens[] = tokenizer.tokenize(text);
        int count = 0;
        int text_len = tokens.length;
        Word w = new Word();
        w.addType(8);
        for (String token : tokens) {
            if (token.length() == 1) {
                text_len--;
            } else if (spellCheck.isInDictionary(token, w)) {
                count++;
            }           
        }
        return (float)count/text_len > 0.6; 
    }
        
//        public List<TableResponse> getCorrectTables(List<TableResponse> tables) throws Exception{
//            List<TableResponse> new_tables = new ArrayList<>();
//            for (TableResponse table : tables){
//                for(TableResponseCell cell : table.getCells()){
//                    cell.setText(getCorrectWord(cell.getText()));
//                } 
//                new_tables.add(table);
//            }
//            return new_tables;
//        }
                
    public String getCorrectWord(String text) throws Exception{
        ArrayList<String> NNPdictionary = getNNPdictionary(text);
        return doSpellCheck(text, NNPdictionary);
    }
        
    private ArrayList<String> getNNPdictionary(String text) throws Exception{
        //Get tokens 
        String tokens[] = tokenizer.tokenize(text);
        //Finding the names in the sentence 
        Span nameSpans[] = nameFinder.find(tokens); 
        return getNNPwords(nameSpans, tokens);
    }

    private ArrayList<String> getNNPwords(Span nameSpans[], String tokens[]){
        ArrayList<String> dictionary = new ArrayList<>();
        for(Span s: nameSpans) {
            for (int i =s.getStart(); i<s.getStart()+s.length(); i++) {
                if (tokens[i].length() > 2)
                    dictionary.add(tokens[i]);
            }
        }
        return dictionary;
    } 
        
    private String doSpellCheck(String input, ArrayList<String> NNPdict) throws SuggesterException{       
        spellCheck.setText(input);
        spellCheck.check();
        while (spellCheck.hasMisspelt()) {
          ArrayList suggestions = spellCheck.getSuggestions();
          if (suggestions.isEmpty()){
              spellCheck.checkNext();
              continue;
          }
          // choose the suggestion
          Suggestion suggestion = (Suggestion) suggestions.get(0);
          String Missplet = spellCheck.getMisspelt();

          if (NNPdict.contains(Missplet)){
              spellCheck.change(Missplet);
          } else {
              spellCheck.change(suggestion.getWord());
          }
          
          spellCheck.checkNext();
        }
        return spellCheck.getText();
    }
}