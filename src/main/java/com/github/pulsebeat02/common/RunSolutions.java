package com.github.pulsebeat02.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RunSolutions {

    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        List<Class<?>> classes = null;
        try {
            classes = getClassesForPackage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert classes != null;
        for (Class<?> clazz : classes) {
            System.out.println("Problem Name: " + clazz.getName());
            long start = System.currentTimeMillis();
            Method method = null;
            try {
                method = clazz.getMethod("main", String[].class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                assert method != null;
                method.invoke(null, (Object) null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start) / 1000);
            System.out.println();
        }
    }

    /**
     * Attempts to list all the classes in the specified package as determined
     * by the context class loader, recursively, avoiding anonymous classes
     *
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException if something went wrong
     */
    private static List<Class<?>> getClassesForPackage() throws ClassNotFoundException {
        List<File> directories = new ArrayList<>();
        String packageToPath = "com.github.pulsebeat02".replace('.', '/');
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            Enumeration<URL> resources = cld.getResources(packageToPath);
            while (resources.hasMoreElements()) {
                directories.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException("com.github.pulsebeat02" + " does not appear to be a valid package (Null pointer exception)");
        } catch (UnsupportedEncodingException encex) {
            throw new ClassNotFoundException("com.github.pulsebeat02" + " does not appear to be a valid package (Unsupported encoding)");
        } catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + "com.github.pulsebeat02");
        }
        List<Class<?>> classes = new ArrayList<>();
        while (!directories.isEmpty()) {
            File directoryFile = directories.remove(0);
            if (directoryFile.exists()) {
                File[] files = directoryFile.listFiles();
                assert files != null;
                for (File file : files) {
                    if ((file.getName().endsWith(".class")) && (!file.getName().contains("$"))) {
                        int index = directoryFile.getPath().indexOf(packageToPath);
                        String packagePrefix = directoryFile.getPath().substring(index).replace('/', '.');
                        try {
                            String className = packagePrefix + '.' + file.getName().substring(0, file.getName().length() - 6);
                            classes.add(Class.forName(className));
                        } catch (NoClassDefFoundError ignored) {}
                    } else if (file.isDirectory()) {
                        directories.add(new File(file.getPath()));
                    }
                }
            } else {
                throw new ClassNotFoundException("com.github.pulsebeat02" + " (" + directoryFile.getPath() + ") does not appear to be a valid package");
            }
        }
        return classes;
    }

}
