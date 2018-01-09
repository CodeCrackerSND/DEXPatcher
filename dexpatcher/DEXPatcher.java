/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dexpatcher;

import org.jf.dexlib.StringIdItem;
import org.jf.dexlib.Util.ByteArrayAnnotatedOutput;
import org.jf.dexlib.Util.FileUtils;
import org.jf.dexlib.*;
import org.jf.dexlib.Util.*;
import org.jf.util.IndentingWriter;
import org.jf.util.LiteralTools;
import org.jf.dexlib.EncodedValue.*;
import org.jf.dexlib.Code.*;
import org.jf.dexlib.Code.Format.*;
import java.util.*;
import java.util.regex.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.jf.dexlib.Code.ReferenceType.*;
/**
 *
 * @author Mihai
 */
public class DEXPatcher {

    // source: https://github.com/aNNiMON/dex-editor
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
String original_file = "D:\\Cracking\\APK\\classes.dex";
DexFile dex=new DexFile(FileUtils.readFile(original_file));

Path path = Paths.get(original_file);
byte[] data = Files.readAllBytes(path);
DexFile.calcSignature(data);
DexFile.calcChecksum(data);

FileOutputStream fos = new FileOutputStream("D:\\Cracking\\APK\\classes_changed.dex");
fos.write(data);
fos.close();

//boolean in_place = dex.getInplace();
dex.setInplace(true);
String out_file = stripExtension(original_file)+"_pached."+GetExtension(original_file);

        String IsTrialClass = "";
        String IsFeatureAvailableClass = "";
        String IsTrialMethodProto = "";
        String IsFeatureAvailableMethodProto = "";
        String IsTrialMethodName = "";
        String IsFeatureAvailableMethodName = "";
        
        IndexedSection<ClassDefItem> classes=dex.ClassDefsSection;
        for(ClassDefItem cl: classes.getItems())
        {
        String cls_name = cl.getClassType().getTypeDescriptor();
        
        if (cls_name.contains("speechnotes/")&&cls_name.contains("MainActivity$c"))  // co.speechnotes.speechnotes.MainActivity.c
        {
        
            ClassDataItem da=cl.getClassData();
            if(da !=null)
            {
                
                ClassDataItem.EncodedMethod[] methods=cl.getClassData().getVirtualMethods();
                for(ClassDataItem.EncodedMethod method :methods)
                {
                
                String prototypeString = method.method.methodPrototype.getPrototypeString();
                if (prototypeString.endsWith(")V")&&!prototypeString.endsWith("()V"))
                {  // if is a void method with 2 parameters:
                
                    Instruction[] instructions=method.codeItem.getInstructions();
                    for (Instruction instruction: instructions)
                    {
                  
                    if (instruction.opcode == Opcode.INVOKE_VIRTUAL)
                    {
                    IsTrialClass = GetClassName(instruction);
                    IsTrialMethodName = GetMethodName(instruction);
                    IsTrialMethodProto = GetMethodProto(instruction);
                    }
                    
                    if (instruction.opcode == Opcode.INVOKE_STATIC)
                    {
                    IsFeatureAvailableClass = GetClassName(instruction);
                    IsFeatureAvailableMethodName = GetMethodName(instruction);
                    IsFeatureAvailableMethodProto = GetMethodProto(instruction);
                    //IsFeatureAvailableMethodProto = IsFeatureAvailableMethodProto.replace(IsFeatureAvailableClass, "");
                    break;
                    }
  
//IsTrial:
//Lco/speechnotes/speechnotes/a/c;
//method c()Z
                    
/*
Method Code Offset: 1053244 -> 10124C
Method: c()Z
{
invoke-virtual {v1} Lco/speechnotes/speechnotes/a/c;->b()Z   // 6E
move-result v0  // 0A
if-nez v0 :label_8  // 39
const/4 v0 1   // 12 10 to 12 00
label_7:
return v0  //  0F 00
label_8:
const/4 v0 0  // 12 00
goto :label_7

}
                    */

                    
//IsFeatureEnabled:
//Lco/speechnotes/speechnotes/MainActivity;
//method a(Lco/speechnotes/speechnotes/MainActivity;Lco/speechnotes/speechnotes/a/d;)Z
                    
                    
/*
Method Code Offset: 1032980 -> FC324
Method: a(Lco/speechnotes/speechnotes/a/d;)Z
{
const/4 v0 1 12 10 - Good boy
if-nez v3 :label_4
label_3:
return v0
label_4:
const-string v1 "premium_features_1_sep2016"
invoke-virtual {v3,v1} Lco/speechnotes/speechnotes/a/d;->b(Ljava/lang/String;)Z
move-result v1
if-nez v1 :label_3
const-string v1 "premium_item_sep2016"
invoke-virtual {v3,v1} Lco/speechnotes/speechnotes/a/d;->b(Ljava/lang/String;)Z
move-result v1
if-nez v1 :label_3
const/4 v0 0
goto :label_3

}
Method Size: 22
*/
                    }
                    
                    
                    
                    }
                    
                    
                
                
                }
             }
        
        }
        
        if (!IsTrialClass.isEmpty()&&!IsFeatureAvailableClass.isEmpty())
        break;  // optimisations
        
        }
        
        if (!IsTrialClass.isEmpty()&&!IsFeatureAvailableClass.isEmpty())
        {
        
        int PatchesCount = 0;
            
        for(ClassDefItem cl: classes.getItems())
        {
        String cls_name = cl.getClassType().getTypeDescriptor();
        
        if (cls_name.equals(IsTrialClass))
        {
            ClassDataItem da=cl.getClassData();
            if(da !=null)
            {
                
                ClassDataItem.EncodedMethod[] methods=cl.getClassData().getVirtualMethods();
                for(ClassDataItem.EncodedMethod method :methods)
                {
                
                if (method.method.methodName.getStringValue().equals(IsTrialMethodName))
                {
                
                String prototypeString = method.method.methodPrototype.getPrototypeString();
                if (prototypeString.equals(IsTrialMethodProto))
                {
                    
                    Instruction[] instructions=method.codeItem.getInstructions();
                    for (int i=0;i<instructions.length;i++)
                    {
                    if (instructions[i].opcode == Opcode.CONST_4)
                    {
                    Instruction11n testingPurpose = (Instruction11n)instructions[i];
                    if (testingPurpose.getLiteral()==1)  // change 1 to 0
                    {
                    // public Instruction11n(Opcode opcode, byte regA, byte litB) {
                    Instruction11n newins = new Instruction11n(Opcode.CONST_4, (byte)0, (byte)0);
                    instructions[i] = newins;
                    //method.codeItem.updateCode(instructions);
                    PatchesCount++;
                    }
                    }
                    }
                
                }
                }
                }
            }
        }
        
        
        if (cls_name.equals(IsFeatureAvailableClass))
        {
            ClassDataItem da=cl.getClassData();
            if(da !=null)
            {
                
                ClassDataItem.EncodedMethod[] methods=cl.getClassData().getDirectMethods();
                for(ClassDataItem.EncodedMethod method :methods)
                {
                
                if (method.method.methodName.getStringValue().equals(IsFeatureAvailableMethodName))
                {
                
                String prototypeString = method.method.methodPrototype.getPrototypeString();

                if (prototypeString.equals(IsFeatureAvailableMethodProto))
                {
                    
                    Instruction[] instructions=method.codeItem.getInstructions();
                    for (int i=0;i<instructions.length;i++)
                    {
                    if (instructions[i].opcode == Opcode.CONST_4)
                    {
                    Instruction11n testingPurpose = (Instruction11n)instructions[i];
                    if (testingPurpose.getLiteral()==0)  // change 0 to 1
                    {
                    // public Instruction11n(Opcode opcode, byte regA, byte litB) {
                    Instruction11n newins = new Instruction11n(Opcode.CONST_4, (byte)0, (byte)1);
                    instructions[i] = newins;
                    //method.codeItem.updateCode(instructions);
                    PatchesCount++;
                    }
                    }
                    }
                
                }
                }
                }
            }
        }
            
        }
        }
        

        dex.setSortAllItems(true);
        dex.place();
        System.out.println("dexfile size : "+dex.getFileSize());
        
        byte[] buf=new byte[dex.getFileSize()];
        ByteArrayAnnotatedOutput out=new ByteArrayAnnotatedOutput(buf);
        dex.writeTo(out);
        byte[] buf2=out.toByteArray();
        System.out.println(""+Arrays.equals(buf,buf2));
        System.out.println(""+buf2.length);
        DexFile.calcSignature(buf2);
        DexFile.calcChecksum(buf2);
        FileOutputStream w=new FileOutputStream(out_file);
        w.write(buf2);
        w.close();
        

        
    }
    
    static String GetClassName(Instruction instruction)
    {
if (instruction.opcode != Opcode.INVOKE_VIRTUAL&&instruction.opcode != Opcode.INVOKE_STATIC)
return "";

if (instruction.opcode.referenceType != ReferenceType.method )
return "";

ReferenceType ref_type = instruction.opcode.referenceType.method;
if (ref_type==null)
return "";

InstructionWithReference ref=(InstructionWithReference)instruction;
MethodIdItem m_id = (MethodIdItem)ref.getReferencedItem();

return m_id.classType.getTypeDescriptor();

    }
    
static String GetMethodProto(Instruction instruction)
    {
if (instruction.opcode != Opcode.INVOKE_VIRTUAL&&instruction.opcode != Opcode.INVOKE_STATIC)
return "";

if (instruction.opcode.referenceType != ReferenceType.method )
return "";

ReferenceType ref_type = instruction.opcode.referenceType.method;
if (ref_type==null)
return "";

InstructionWithReference ref=(InstructionWithReference)instruction;
MethodIdItem m_id = (MethodIdItem)ref.getReferencedItem();

return m_id.methodPrototype.getPrototypeString();

    }

static String GetMethodName(Instruction instruction)
    {
if (instruction.opcode != Opcode.INVOKE_VIRTUAL&&instruction.opcode != Opcode.INVOKE_STATIC)
return "";

if (instruction.opcode.referenceType != ReferenceType.method )
return "";

ReferenceType ref_type = instruction.opcode.referenceType.method;
if (ref_type==null)
return "";

InstructionWithReference ref=(InstructionWithReference)instruction;
MethodIdItem m_id = (MethodIdItem)ref.getReferencedItem();

return m_id.getMethodName().getStringValue();

    }
    
       static String stripExtension (String str)
       {
        // Handle null case specially.
        if (str == null) return null;
        
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }
      
       static String GetExtension (String fileName)
       {
String extension = "";

int i = fileName.lastIndexOf('.');
int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

if (i > p)
{
    extension = fileName.substring(i+1);
}

return extension;
    
       }
}
