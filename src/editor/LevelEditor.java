package editor;

import Util.EnvironmentFloat;
import javax.vecmath.Vector3f;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Lucas on 06/05/2017.
 */
public class LevelEditor extends JFrame {

    private final static int EMPTY = 0;
    private final static int START = 3;
    private final static int END = 2;
    private final static int OBSTACLE = 1;
    private final static Color[] color={Color.WHITE,Color.BLACK,Color.GREEN,Color.RED};
    private final static Color[] semiTransparentColor={
            null,
            new Color(Color.BLACK.getRed(),Color.BLACK.getBlue(),Color.BLACK.getGreen(),126),
            new Color(Color.GREEN.getRed(),Color.GREEN.getBlue(),Color.GREEN.getGreen(),126),
            new Color(Color.RED.getRed(),Color.RED.getBlue(),Color.RED.getGreen(),126)};
    private volatile int selected=START;
    private volatile int level[][][];
    private volatile int height = 0;





    public LevelEditor(final int x,final int y,final int z){
        class ToggleButton extends JButton
        {
            public ToggleButton(final int a)
            {
                setMinimumSize(new Dimension(64,64));
                setBackground(color[a]);
                setOpaque(true);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selected=a;
                    }
                });
            }
        }
        level = new int[x][y][z];
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(new JPanel()
                {
                    private int i=0;
                    private int BUTTON_HEIGHT=64;
                    private int BUTTON_WIDTH=64;
                    {
                        setLayout(null);
                        add(new ToggleButton(START)
                        {
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                            }
                        });
                        add(new ToggleButton(END)
                        {
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                            }
                        });
                        add(new ToggleButton(OBSTACLE)
                        {
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                            }
                        });
                        add(new JButton("up")
                        {
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        height++;
                                        if(height>=y)
                                        {
                                            height=y-1;
                                        }
                                        LevelEditor.this.repaint();
                                    }
                                });
                            }
                        });
                        add(new JButton("down")
                        {
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        height--;
                                        if(height<0)
                                        {
                                            height=0;
                                        }
                                        LevelEditor.this.repaint();
                                    }
                                });
                            }
                        });
                        add(new JButton("save")
                        {
                            JFileChooser chooser=new JFileChooser()
                            {
                                {
                                    setCurrentDirectory(new java.io.File("."));
                                    setDialogTitle("set save directory");
                                    setFileSelectionMode(DIRECTORIES_ONLY);
                                }
                            };
                            {
                                setBounds(0,BUTTON_HEIGHT*(i++),BUTTON_WIDTH,BUTTON_HEIGHT);
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if(chooser.showOpenDialog(LevelEditor.this)==JFileChooser.APPROVE_OPTION)
                                        {
                                            File directory=chooser.getSelectedFile();
                                            if(directory!=null) {
                                                java.util.List<Vector3f> obstacles = new LinkedList<>();
                                                java.util.List<Vector3f> startconfig = new LinkedList<>();
                                                java.util.List<Vector3f> endconfig = new LinkedList<>();


                                                for (int i = 0; i < level.length; i++) {
                                                    for (int j = 0; j < level[0].length; j++) {
                                                        for (int k = 0; k < level[0][0].length; k++) {
                                                            if (level[i][j][k] == OBSTACLE) {
                                                                obstacles.add(new Vector3f(i, j, k));
                                                            }
                                                            if (level[i][j][k] == START) {
                                                                startconfig.add(new Vector3f(i, j, k));
                                                            }
                                                            if (level[i][j][k] == END) {
                                                                endconfig.add(new Vector3f(i, j, k));
                                                            }
                                                        }
                                                    }
                                                }

                                                EnvironmentFloat environmentInt = new EnvironmentFloat(new Vector3f(x, y, z), obstacles, startconfig, endconfig);
                                                try {
                                                    environmentInt.environmentToFile(new File(directory.toString() + File.separator + "enviroment size.project"),
                                                            new File(directory.toString() + File.separator + "obstacles.project"),
                                                            new File(directory.toString() + File.separator + "configuration.project"));
                                                } catch (IOException err) {
                                                    err.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT*(i++)));
                    }
                }),
                new JScrollPane(new JPanel()
                {
                    private final static int BLOCK_MIN_SIDE=16;
                    private volatile int blockSize=BLOCK_MIN_SIDE;
                    {
                        setPreferredSize(new Dimension(BLOCK_MIN_SIDE*x+1,BLOCK_MIN_SIDE*z+1));
                        addMouseListener(new MouseAdapter() {
                                             @Override
                                             public void mouseClicked(MouseEvent e) {
                                                 int x1=e.getX()/blockSize;
                                                 int y1=e.getY()/blockSize;
                                                 if(e.getButton()==MouseEvent.BUTTON1 && x1>=0 && x1<x && y1>=0 && y1<z)
                                                 {
                                                    if(level[x1][height][y1]!=EMPTY)
                                                    {
                                                        level[x1][height][y1]=EMPTY;
                                                    }
                                                    else
                                                    {
                                                        level[x1][height][y1]=selected;
                                                    }
                                                    repaint();
                                                 }
                                             }
                                             public void mouseDragged(MouseEvent e)
                                             {
                                                 int x1=e.getX()/blockSize;
                                                 int y1=e.getY()/blockSize;
                                                 if(x1>=0 && x1<x && y1>=0 && y1<y)
                                                 {
                                                     if(level[x1][height][y1]==EMPTY && e.getButton()==MouseEvent.BUTTON1)
                                                     {
                                                         level[x1][height][y1]=selected;
                                                     }
                                                     else if(level[x1][height][y1]!=EMPTY && e.getButton()==MouseEvent.BUTTON2)
                                                     {
                                                         level[x1][height][y1]=EMPTY;
                                                     }
                                                     repaint();
                                                 }
                                             }
                                         }
                        );
                        addComponentListener(new ComponentAdapter()
                        {
                            @Override
                            public void componentResized(ComponentEvent e)
                            {
                                int tempBlockSide;
                                if(getWidth()*z>getHeight()*x)
                                {
                                    tempBlockSide=getHeight()/z;
                                }
                                else
                                {
                                    tempBlockSide=getWidth()/x;
                                }
                                blockSize=Math.max(tempBlockSide,BLOCK_MIN_SIDE);
                                repaint();
                            }
                        });
                    }
                    public void paintComponent(Graphics g)
                    {
                        g.setColor(Color.GRAY);
                        g.fillRect(0,0,getWidth(),getHeight());
                        for(int i=0;i<x;i++)
                        {
                            for(int j=0;j<z;j++)
                            {
                                drawSquare(g,i,j,height,0);
                            }
                        }
                    }
                    public void drawSquare(Graphics g,int x, int y,int height,int depth)
                    {
                        if(height<0)
                        {
                            g.setColor(color[EMPTY]);
                            g.fillRect(x*blockSize,y*blockSize,blockSize,blockSize);
                            g.setColor(Color.BLACK);
                            g.drawRect(x*blockSize,y*blockSize,blockSize,blockSize);
                        }
                        else if(level[x][height][y]==EMPTY)
                        {
                            drawSquare(g,x,y,height-1,depth+1);
                        }
                        else
                        {
                            float alpha=(float)Math.pow(0.5,depth);
                            Color c=color[level[x][height][y]];
                            float[] colorComponents=c.getColorComponents(new float[4]);
                            float[] whiteComponents=Color.WHITE.getComponents(new float[4]);
                            float[] result=new float[4];
                            for(int i=0;i<colorComponents.length;i++)
                            {
                                result[i]=alpha*colorComponents[i]+(1-alpha)*whiteComponents[i];
                            }
                            g.setColor(new Color(result[0],result[1],result[2]));
                            g.fillRect(x*blockSize,y*blockSize,blockSize,blockSize);
                            g.setColor(Color.BLACK);
                            g.drawRect(x*blockSize,y*blockSize,blockSize,blockSize);
                            g.drawString(""+(-depth),x*blockSize,(y+1)*blockSize);
                        }
                    }
                }))
        );
        pack();
        setVisible(true);
    }
    public static void main(String[] args) {
        new LevelEditor(9,9,9);
    }
}
