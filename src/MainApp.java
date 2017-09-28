import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

//TODO: найти полное паросочетание
//TODO: делаем сеть - то есть вводим s и t, соединяем s со всеми из x, x - с теми, которые можем - из y
//TODO: и все y - с t.
//TODO: По Форду-Фалкерсону ищем максимальный поток
//TODO: берём новые соединения x с y - PROFIT


public class MainApp
{
    static int firstSetSize, secondSetSize, arraySize;
    static ArrayList<Vertex> firstSet = new ArrayList<>();
    static ArrayList<Vertex> secondSet = new ArrayList<>();
    static ArrayList<Vertex> vertices = new ArrayList<>();

    public static void main(String[] args)
    {
//        makeGraph(getData());
        makeGraph(getData());
        printAdjList();
        System.out.println(maxBPM());
        System.out.println("ANSWER");
        for (int i = 0; i < firstSetSize; i++) {
            System.out.print(firstSet.get(i).getPoint()+":");
            System.out.println(firstSet.get(i).getAttachedVertex().getPoint());
        }
    }

    //returns 0 if can't otherwise returns the vertex point
    //почти дфс
    //TODO: добавить грёбанную рекурсию, а то ничего не найдёт максимального
    public static boolean linkCanBeFormed(Vertex x, ArrayList<Vertex> seen)
    {
        //we get every vertex from x
        for (int i = 0; i < secondSetSize; i++) {
            //if they are connected and y is not visited
            if (x.getConnectedVertices().contains(secondSet.get(i)) && !seen.contains(secondSet.get(i)))
            {
                seen.add(secondSet.get(i));
                //плохая проверка secondSet.get(i).getAttachedVertex() == null
                if (findAttachedVertex(secondSet.get(i))==null ||
                        linkCanBeFormed(findAttachedVertex(secondSet.get(i)), seen)) //if y is single we just connect them
                {
                    secondSet.get(i).setAttached(true);
                    x.setAttachedVertex(secondSet.get(i));
                    return true;
                }
            }
        }
        return false;
    }

    public static Vertex findAttachedVertex(Vertex y)
    {
        for (int i = 0; i < firstSetSize; i++) {
            if (firstSet.get(i).getAttachedVertex()!=null && firstSet.get(i).getAttachedVertex().getPoint() == y.getPoint())
                return firstSet.get(i);
        }
        return null;
    }

    // Returns maximum number of matching from M to N
    public static int maxBPM()
    {
        // An array to keep track of the applicants assigned to
        // jobs. The value of matchR[i] is the applicant number
        // assigned to job i, the value -1 indicates nobody is
        // assigned.
        ArrayList<Integer> match = new ArrayList<>(); //массив - к какой работе кто привязан, не привязан -1
        for (int i = 0; i < secondSetSize; i++) { // Initially all jobs are available
            match.add(-1);
        }
        int result = 0; // Count of jobs assigned to applicants
        for (int u = 0; u < firstSetSize; u++)
        {
            // Mark all jobs as not seen for next applicant.
            ArrayList<Vertex> seen = new ArrayList<>();
            for(int i=0; i<secondSetSize; ++i)
                seen.add(null);

            // Find if the applicant 'u' can get a job
            if (linkCanBeFormed(firstSet.get(u), seen))
                result++;
        }
        return result;
    }

    public static String getData()
    {
        String line = null;
        File file = new File("in.txt");
        StringBuilder sb = new StringBuilder();

        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exists");
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void createEmptyVerticesArray()
    {
        for (int i = 0; i < firstSetSize + secondSetSize; i++) {
            Vertex vertex = new Vertex(i+1);
            vertices.add(vertex);
        }
    }

    public static void getFirstSet(ArrayList<String> lines)
    {
        for (int i = 0; i < lines.size(); i++) {
            Vertex vertex = new Vertex(Integer.parseInt(lines.get(i).split("\\s+")[0]));
            firstSet.add(vertex);
        }
    }

    public static void createSets()
            //TODO: Since we don't know shit about their names we shudn't name the second set OurSelves
            //or maybe we can why not
    {
        for (int i = 0; i < firstSetSize; i++) {
            Vertex vertex = new Vertex(i+1);
            vertex.setFirstSet(true);
            vertex.setAttached(false);
            firstSet.add(vertex);
            vertices.add(vertex);
        }
        for (int i = 0; i < secondSetSize; i++) {
            Vertex vertex = new Vertex(i+1);
            vertex.setFirstSet(false);
            vertex.setAttached(false);
            secondSet.add(vertex);
            vertices.add(vertex);
        }
    }

    public static void makeGraph(String s)
    {
        String[] stringArray = s.split(";");
        firstSetSize = Integer.parseInt(stringArray[0].split("\\s+")[0]);
        secondSetSize = Integer.parseInt(stringArray[0].split("\\s+")[1]);
        arraySize = Integer.parseInt(stringArray[1]);
        createSets();
        for (int i = 0; i < stringArray.length; i++) {
            System.out.println(stringArray[i]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < stringArray.length; i++) {
            sb.append(stringArray[i]+" ");
        }

        ArrayList<String> arrStrings = new ArrayList<>(Arrays.asList(sb.toString().split("\\s+")));
        //we need to create array of pointers here
        ArrayList<Integer> pointersArray = new ArrayList<>();
        for (int i = 0; i < firstSetSize; i++) {
            int pointer = Integer.parseInt(arrStrings.get(i));
            pointersArray.add(pointer);
        }
        //need to connect 'em somehow
        int startPointForY = firstSetSize;
        for (int i = 0; i < pointersArray.size(); i++) {
            int difference;
            if (i < pointersArray.size()-1) {
                difference = pointersArray.get(i + 1) - pointersArray.get(i);
            }
            else {
                difference = arraySize-pointersArray.get(i);
            }
            Vertex xVertex = firstSet.get(i);
            //5 7 9 10 1 2 2 3 3 32767
            for (int j = pointersArray.get(i)-1; j < pointersArray.get(i)+difference-1; j++) {
                int toFindVertice = Integer.parseInt(arrStrings.get(j));
//                Vertex yVertex = vertices.get(j-1);
//                Vertex yVertex = vertices.get(vertices.indexOf(new Vertex(toFindVertice)));
                //TODO: найти вершину в одном из статических массивов по точке
                Vertex yVertex = findVertexByPoint(toFindVertice);
                xVertex.connectVertices(yVertex);
            }
        }
//        //TODO: I don't need to parse here that way, just create a Vertex class + Vertex arrayList
//        ArrayList<String> list = new ArrayList<>(Arrays.asList(s.split(";")));
//        firstSetSize = Integer.parseInt(list.get(0).split("\\s+")[0]);
//        secondSetSize = Integer.parseInt(list.get(0).split("\\s+")[1]);
//        arraySize = Integer.parseInt(list.get(1));
//        createEmptyVerticesArray();
//        list.remove(list.get(0));
//        list.remove(list.get(0));
//        list.remove(list.get(list.size()-1));
//        getFirstSet(list);
//        //TODO: вот здесь надо что-то сделать нормальное
//        //TODO: главная проблема в том, что мы каждый раз создаём новую вершину для этого всего
//        //TODO: ещё плохо, что у нас идут вершины по порядку, а не из X
//        //TODO: возможно, что эти вершины надо выковыривать в отдельном методе
////        for (int i = 2; i < arraySize+2; i++) {
////            String[] lineArray = list.get(i-2).split("\\s+");
////            ArrayList<Integer> lineList = new ArrayList<>();
////
////            for (int j = 0; j < lineArray.length; j++) {
////                lineList.add(Integer.parseInt(lineArray[j]));
////            }
////            //всё равно создаёт новые вершины
////            Vertex vertex = firstSet.get(i-2);
////            //TODO: что-то не то с выбором вершин
////            for (int j = 1; j < lineList.size()-1; j++) {
////                Vertex secondVertex = vertices.get(lineList.get(j));
////                vertex.connectVertices(secondVertex);
////            }
//////            firstSet.add(vertex);
////        }
//        //TODO: лучше это всё переписать нафиг
//        //нам надо получить 3 вершины из Х и чтобы к ним были прилеплены из Y
//        for (int i = 0; i < arraySize; i++) {
//            //дичь
//        }
    }

    public static Vertex findVertexByPoint(int point)
    {
        for (int i = 0; i < secondSet.size(); i++) {
            if (secondSet.get(i).getPoint()==point)
                return secondSet.get(i);
        }
        return null;
    }

    public static void printAdjList()
    {
        for (int i = 0; i < firstSetSize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < firstSet.get(i).getConnectedVertices().size(); j++) {
                sb.append(firstSet.get(i).getConnectedVertices().get(j).getPoint()+",");
            }
            System.out.println(i+1+":"+sb);
        }
    }

}
