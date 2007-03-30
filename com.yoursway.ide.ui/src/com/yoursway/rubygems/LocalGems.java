package com.yoursway.rubygems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvyaml.nodes.Node;
import org.jvyaml.nodes.ScalarNode;

import com.yoursway.utils.YamlUtil;

public class LocalGems implements ILocalGems {
    
    private static final String[] EMPTY_STRING_LIST = new String[0];
    private static final IGem[] EMPTY_IGEM_LIST = new IGem[0];
    
    @SuppressWarnings("unchecked")
    public String[] parseGemVersions(String serializedGems) {
        Node yamlData = YamlUtil.parseYaml(serializedGems);
        
        ArrayList<String> versions = new ArrayList<String>();
        
        for (Node gemInfo : (List<Node>) yamlData.getValue()) {
            Map<Node, Node> gemAttributes = (Map<Node, Node>) gemInfo.getValue();
            for (Entry<Node, Node> attributeEntry : gemAttributes.entrySet())
                if ("version".equals((String) attributeEntry.getKey().getValue())) {
                    versions.add((String) attributeEntry.getValue().getValue());
                    break;
                }
        }
        return versions.toArray(EMPTY_STRING_LIST);
    }
    
    @SuppressWarnings("unchecked")
    public IGem[] parseGemsInfo(String serializedGems) {
        Node yamlData = YamlUtil.parseYaml(serializedGems);
        
        ArrayList<IGem> gems = new ArrayList<IGem>();
        
        for (Node gemInfo : (List<Node>) yamlData.getValue()) {
            Gem gem = new Gem();
            Map<Node, Node> gemAttributes = (Map<Node, Node>) gemInfo.getValue();
            for (Entry<Node, Node> attributeEntry : gemAttributes.entrySet()) {
                String attributeName = (String) attributeEntry.getKey().getValue();
                Object attributeValue = attributeEntry.getValue().getValue();
                if ("version".equals(attributeName))
                    gem.setVersion((String) attributeValue);
                else if ("name".equals(attributeName))
                    gem.setName((String) attributeValue);
                else if ("directory".equals(attributeName))
                    gem.setDirectory((String) attributeValue);
                else if ("require_path".equals(attributeName)) {
                    ArrayList<String> paths = new ArrayList<String>();
                    for (Node path : (ArrayList<ScalarNode>) attributeValue)
                        paths.add((String) path.getValue());
                    gem.setRequirePaths(paths.toArray(EMPTY_STRING_LIST));
                }
            }
            gems.add(gem);
        }
        
        return gems.toArray(EMPTY_IGEM_LIST);
    }
}
