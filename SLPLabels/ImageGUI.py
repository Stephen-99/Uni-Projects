import threading
import time
from PIL import Image, ImageTk
import tkinter as tk

class ImageGUI(threading.Thread):
    def __init__(self):
        super().__init__()
        self.image_path = None
        self.stop_thread = False
        self.displaying = False
        self.root = None
        self.label = None

    def set_image_path(self, image_path):
        self.image_path = image_path
        self.displaying = True

    def run(self):
        self.root = tk.Tk()
        self.root.title("Image Display")
        self.root.geometry("+0+0")

        self.label = tk.Label(self.root)
        self.label.pack()

        self.update_gui()
        self.root.mainloop()

    def update_gui(self):
        if self.displaying:
            image = Image.open(self.image_path)
            photo = ImageTk.PhotoImage(image)
            self.label.configure(image=photo)
            self.label.image = photo  # Prevent PhotoImage from being garbage collected
            self.displaying = False

        if not self.stop_thread:
            self.root.after(100, self.update_gui)  # Schedule the next update
        else:
            self.root.destroy()

    def stop(self):
        self.stop_thread = True
        self.image_path = None
        self.label = None

